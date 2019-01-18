package com.noadd.myapp.service.qzone.impl;

import com.noadd.myapp.mailservice.LogToMail;
import com.noadd.myapp.mailservice.MyMailService;
import com.noadd.myapp.redis.RedisManager;
import com.noadd.myapp.service.qzone.QzoneService;
import com.noadd.myapp.util.QzoneUtil;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 *
 **/
@Service
public class QzoneServiceImpl implements QzoneService {
    private static String PATH = "/opt/www/qrcode/";
    //    private static String PATH = "E:/code/myproject/target/myapp-0.0.1-SNAPSHOT/qrcode/";
    private final RedisManager redisManager;
    private static String COOKIEPRE = "QZCOOKIE";
    private static String MZSTATEPRE = "MZSTATE";

    @Autowired
    public QzoneServiceImpl(RedisManager redisManager) {
        this.redisManager = redisManager;
    }


    @Override
    public String getQrCode(String qq) throws Exception {
        QzoneUtil qzoneUtil = new QzoneUtil();
        //生成二维码
        qzoneUtil.ptqrshow(PATH + qq + ".png");
        saveCookie(qq, qzoneUtil);
        return qq;
    }

    private void saveCookie(String qq, QzoneUtil qzoneUtil) {
        List<Cookie> cookieList = qzoneUtil.getContext().getCookieStore().getCookies();
        redisManager.update(COOKIEPRE + qq, cookieList);
    }

    @Override
    public String login(String qq) throws Exception {
        List<Cookie> cookieMapList = (List<Cookie>) redisManager.select(COOKIEPRE + qq);
        if (cookieMapList == null) {
            return "-1";
        }
        QzoneUtil qzoneUtil = new QzoneUtil(cookieMapList);
        qq = qzoneUtil.loginOnce(PATH + qq + ".png");
        if (!"0".equals(qq)) {
            saveCookie(qq, qzoneUtil);
        }
        return qq;
    }

    @Autowired
    MyMailService mailService;
    @Autowired
    private LogToMail logToMail;

    @Override
    public String miaoZhan(String qq) throws Exception {
        List<Cookie> cookieMapList = (List<Cookie>) redisManager.select(COOKIEPRE + qq);
        if (cookieMapList == null) {
            return "-1";
        }
        QzoneUtil qzoneUtil = new QzoneUtil(cookieMapList);
        if (redisManager.select(MZSTATEPRE + qq) == null) {
            new Thread(() -> {
                try {
                    redisManager.update(MZSTATEPRE + qq, true);
                    qzoneUtil.miaoZhan(MZSTATEPRE + qq, redisManager);
                    mailService.sendSimpleMail(qq + "@qq.com", "秒赞登录超时提醒", "QQ:" + qq + ",秒赞服务已停止，" +
                            "登录状态已失效");
                } catch (Exception e) {
                    redisManager.delete(COOKIEPRE + qq);
                    redisManager.delete(MZSTATEPRE + qq);
                    mailService.sendSimpleMail(qq + "@qq.com", "秒赞错误提醒", "QQ:" + qq + ",秒赞服务已停止，" +
                            "发现未知错误");
                    logToMail.error("秒赞报错,QQ:" + qq, e);
                }
            }) {
            }.start();
        }
        return "1";
    }
}
