package com.noadd.myapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@MapperScan("com.noadd.myapp.domain.mapper")
@EnableTransactionManagement
@EnableScheduling
public class MyappApplication {



//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        List<String> urlPatterns = new ArrayList<>();
//        MyWebFilter myWebFilter = new MyWebFilter();
//        urlPatterns.add("/*");
//        filterRegistrationBean.setFilter(myWebFilter);
//        filterRegistrationBean.setUrlPatterns(urlPatterns);
//        return filterRegistrationBean;
//    }
}
