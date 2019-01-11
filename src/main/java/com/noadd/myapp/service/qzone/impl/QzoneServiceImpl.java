package com.noadd.myapp.service.qzone.impl;

import com.noadd.myapp.redis.RedisManager;
import com.noadd.myapp.service.qzone.QzoneService;
import com.noadd.myapp.util.QzoneUtil;
import org.apache.http.client.protocol.HttpClientContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 *
 **/
@Service
public class QzoneServiceImpl implements QzoneService {
    @Autowired
    RedisManager redisManager;
        private static String PATH = "/opt/www/qrcode";
//    private static String PATH = "C:\\Users\\Administrator\\Desktop\\";

    @Override
    public String getQrCode(String qq) throws Exception {
        QzoneUtil qzoneUtil = new QzoneUtil();
        //生成二维码
        String qrPath = qzoneUtil.ptqrshow(PATH + qq + ".png");
        qzoneUtil.login(PATH + qrPath);
        new Thread(() -> {
            try {
                qzoneUtil.miaoZhan();
            } catch (Exception e) {
                redisManager.delete(qq);
            }
        }) {
        }.start();
        return qrPath;
    }

    @Override
    public String login(String qq, String qrPath) throws Exception {
        QzoneUtil qzoneUtil = new QzoneUtil();
        qzoneUtil.setContext((HttpClientContext) redisManager.select(qq));

        redisManager.update(qq, qzoneUtil.getContext());
        return qq;
    }

    @Override
    public void miaoZhan(String qq) {
        QzoneUtil qzoneUtil = new QzoneUtil();
        qzoneUtil.setContext((HttpClientContext) redisManager.select(qq));

    }
}
