package com.noadd.myapp;

import com.noadd.myapp.service.qzone.QzoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 程序初始之后运行
 **/
@Component
@Order(value = 0)
public class InitSystemConfig implements CommandLineRunner {
    @Autowired
    QzoneService qzoneService;

    @Override
    public void run(String... args) throws Exception {
        qzoneService.miaoZhan();
        qzoneService.addMiaoZhan();
//        qzoneService.zdZhan();
    }
}
