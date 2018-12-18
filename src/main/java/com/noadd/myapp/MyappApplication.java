package com.noadd.myapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.noadd.myapp.domain.mapper")
@EnableTransactionManagement
@EnableScheduling
public class MyappApplication {
}
