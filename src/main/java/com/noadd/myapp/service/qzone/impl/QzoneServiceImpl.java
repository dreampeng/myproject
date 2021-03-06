package com.noadd.myapp.service.qzone.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.noadd.myapp.mailservice.LogToMail;
import com.noadd.myapp.mailservice.MyMailService;
import com.noadd.myapp.redis.RedisManager;
import com.noadd.myapp.service.qzone.QzoneService;
import com.noadd.myapp.util.QzoneUtil;
import com.noadd.myapp.util.baseUtil.StringUtil;
import org.apache.http.cookie.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *
 **/
@Service
public class QzoneServiceImpl implements QzoneService {
    private static String PATH = "/opt/www/qrcode/";
    //        private static String PATH = "E:/code/myproject/target/myapp-0.0.1-SNAPSHOT/qrcode/";
    private final RedisManager redisManager;
    private final MyMailService myMailService;
    private static String COOKIEPRE = "QZCOOKIE";
    private static String MZSLIST = "MZSLIST";
    private static String ZDSLIST = "ZDSLIST";
    private static String MZWAIT = "MZWAIT";
    private static String ZDWAIT = "ZDWAIT";

    @Autowired
    public QzoneServiceImpl(RedisManager redisManager, MyMailService myMailService) {
        this.myMailService = myMailService;
        this.redisManager = redisManager;
    }


    @Override
    public String getQrCode(String qq) throws Exception {
        List<String> qqList = (List<String>) redisManager.select(MZSLIST);
        if (qqList.contains(qq)) {
            return "-1";
        }
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
            saveCookie(qq, qzoneUtil);
            redisManager.update(MZWAIT + qq, qq);
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
            while (true) {
                List<String> qqList = (List<String>) redisManager.select(MZSLIST);
                if (qqList == null) {
                    qqList = new ArrayList<>();
                }
                Iterator<String> it = qqList.iterator();
                while (it.hasNext()) {
                    String qq = it.next();
                    try {
                        List<Cookie> cookieMapList = (List<Cookie>) redisManager.select(COOKIEPRE + qq);
                        if (cookieMapList == null) {
                            continue;
                        }
                        QzoneUtil qzoneUtil = new QzoneUtil(cookieMapList);
                        int result = qzoneUtil.miaoZhan();
                        //登录失败或者未知错误
                        if (result == -1) {
                            redisManager.delete(COOKIEPRE + qq);
                            System.out.println("移除QQ：" + qq);
                            it.remove();
                            mailService.sendSimpleMail(qq + "@qq.com", "秒赞登录超时提醒", "QQ:" + qq + ",秒赞服务已停止，" +
                                    "登录状态已失效");
                        }
                        if (result == -2) {
                            System.out.println("报错QQ：" + qq);
                        }
                        qzoneUtil.headLike();
                    } catch (Exception e) {
                        e.printStackTrace();
                        logToMail.error("秒赞报错", e);
                    }
                }
                try {
                    if (qqList.size() < 6) {
                        Thread.sleep(2500);
                    } else if (qqList.size() < 11) {
                        Thread.sleep(1000);
                    } else if (qqList.size() < 15) {
                        Thread.sleep(200);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }) {
        }.start();
    }

    @Override
    public JSONArray getMiaoZhan() {
        List<String> qqList = (List<String>) redisManager.select(MZSLIST);
        if (qqList == null) {
            qqList = new ArrayList<>();
        }
        JSONArray result = new JSONArray();
        for (String s : qqList) {
            JSONObject temp = new JSONObject();
            temp.put("QQ:", s);
            result.add(temp);
        }
        return result;
    }

    @Override
    public void addMiaoZhan() {
        new Thread(() -> {
            while (true) {
                //秒赞列表
                List<String> qqList = (List<String>) redisManager.select(MZSLIST);
                if (qqList == null) {
                    qqList = new ArrayList<>();
                }
                List<Object> waits = redisManager.selects(MZWAIT + "*");
                for (Object wait : waits) {
                    String qq = (String) wait;
                    qqList.add(qq);
                    System.out.println("新增秒赞QQ:" + qq);
                    redisManager.delete(MZWAIT + qq);
                }
                Iterator it = qqList.iterator();
                while (it.hasNext()) {
                    String qq = (String) it.next();
                    List<Cookie> cookieMapList = (List<Cookie>) redisManager.select(COOKIEPRE + qq);
                    if (cookieMapList == null) {
                        it.remove();
                        System.out.println("删除QQ:" + qq);
                    }
                }
                redisManager.update(MZSLIST, qqList);
                //指定赞列表
                List<String> zdList = (List<String>) redisManager.select(ZDSLIST);
                if (zdList == null) {
                    zdList = new ArrayList<>();
                }
                List<Object> zdWaits = redisManager.selects(ZDWAIT + "*");
                for (Object wait : zdWaits) {
                    String qq = (String) wait;
                    zdList.add(qq);
                    System.out.println("新增指定QQ:" + qq);
                    redisManager.delete(ZDWAIT + qq);
                }
                redisManager.update(ZDSLIST, zdList);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void delMz(String qq) {
        redisManager.delete(COOKIEPRE + qq);
    }

    @Override
    public void addZhan(String qq) {
        redisManager.update(ZDWAIT + qq, qq);
    }

    @Override
    public void zdZhan() {
        new Thread(() -> {
            while (true) {
                List<String> qqList = (List<String>) redisManager.select(ZDSLIST);
                if (qqList == null) {
                    qqList = new ArrayList<>();
                }
                if (qqList.size() < 1) {
                    continue;
                }
                Iterator<String> it = qqList.iterator();
                while (it.hasNext()) {
                    String qq = it.next();
                    try {
                        List<Object> cookieLists = redisManager.selects(COOKIEPRE + "*");
                        if (cookieLists != null && cookieLists.size() > 0) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (Object cookieList : cookieLists) {
                                if (cookieList == null) {
                                    continue;
                                }
                                QzoneUtil qzoneUtilTemp = new QzoneUtil((List<Cookie>) cookieList);
                                String loginQq = (String) qzoneUtilTemp.getCookieMap().get("qq_num");
                                JSONObject alls = qzoneUtilTemp.getAllSs(qq, 1);
                                String code = alls.getString("code");
                                if ("-10031".equals(code)) {
                                    System.out.println(loginQq + "对于" + qq + "没有访问权限");
                                    stringBuilder.append(loginQq).append("没有访问权限");
                                } else if ("0".equals(code)) {
                                    List<String> tids = (List<String>) alls.get("tidList");
                                    tids.forEach(tid -> {
                                        if (qzoneUtilTemp.doLike(loginQq, qq, tid, true)) {
                                            System.out.println(loginQq + "----" + qq + "----" + tid);
                                        }
                                    });
                                    stringBuilder.append(loginQq).append("给你点赞").append(tids.size()).append("条\n");
                                } else if ("-3000".equals(code)) {
                                    myMailService.sendSimpleMail(loginQq + "@qq.com",
                                            "秒赞登录已经失效了哦，请重新登录",
                                            "你在 http://www.apesing.com/qzone/ 的登录状态已经失效了哦，" +
                                                    "请访问地址重新登录");
                                }
                            }
                            if (!StringUtil.isEmpty(stringBuilder.toString())) {
                                myMailService.sendSimpleMail(qq + "@qq.com",
                                        "说说点赞信息反馈",
                                        stringBuilder.toString());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logToMail.error("自动点赞报错", e);
                    }
                    it.remove();
                }
                redisManager.update(ZDSLIST, qqList);
            }
        }) {
        }.start();
    }
}
