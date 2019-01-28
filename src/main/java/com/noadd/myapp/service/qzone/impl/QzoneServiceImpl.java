package com.noadd.myapp.service.qzone.impl;

import com.noadd.myapp.mailservice.LogToMail;
import com.noadd.myapp.mailservice.MyMailService;
import com.noadd.myapp.redis.RedisManager;
import com.noadd.myapp.service.qzone.QzoneService;
import com.noadd.myapp.util.QzoneUtil;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
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
    private static String MZSLIST = "MZSLIST";

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
        redisManager.delete(COOKIEPRE + qq);
        if (!"0".equals(qq)) {
            List<String> qqList = (List<String>) redisManager.select(MZSLIST);
            if (qqList == null) {
                qqList = new ArrayList<>();
            }
            if (!qqList.contains(qq)) {
                qqList.add(qq);
                redisManager.update(MZSLIST, qqList);
            }
            saveCookie(qq, qzoneUtil);
        }
        return qq;
    }

    @Autowired
    MyMailService mailService;
    @Autowired
    private LogToMail logToMail;

    @Override
    public void miaoZhan() {
        new Thread(() -> {
            List<String> qqList = (List<String>) redisManager.select(MZSLIST);
            if (qqList == null || qqList.size() < 1) {
                qqList = new ArrayList<>();
            }
            while (true) {
                Iterator<String> it = qqList.iterator();
                while (it.hasNext()) {
                    String qq = it.next();
                    System.out.println("QQ:" + qq + ",开始");
                    try {
                        List<Cookie> cookieMapList = (List<Cookie>) redisManager.select(COOKIEPRE + qq);
                        if (cookieMapList == null) {
                            it.remove();
                            mailService.sendSimpleMail(qq + "@qq.com", "秒赞登录超时提醒", "QQ:" + qq + ",秒赞服务已停止，" +
                                    "登录状态已失效");
                            continue;
                        }
                        QzoneUtil qzoneUtil = new QzoneUtil(cookieMapList);
                        int result = qzoneUtil.miaoZhan();
                        //登录失败或者未知错误
                        if (result == -1 || result == -2) {
                            redisManager.delete(COOKIEPRE + qq);
                            it.remove();
                            if (result == -1) {
                                mailService.sendSimpleMail(qq + "@qq.com", "秒赞登录超时提醒", "QQ:" + qq + ",秒赞服务已停止，" +
                                        "登录状态已失效");
                            } else {
                                mailService.sendSimpleMail(qq + "@qq.com", "秒赞错误提醒", "QQ:" + qq + ",秒赞服务已停止，" +
                                        "发现未知错误");
                            }
                            redisManager.update(MZSLIST, qqList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logToMail.error("秒赞报错", e);
                    }
                    qqList = (List<String>) redisManager.select(MZSLIST);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }) {
        }.start();
    }
}
