package com.noadd.myapp;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private MyWebInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //白名单
        List<String> excldePath = new ArrayList();
        //过滤所有静态文件
        excldePath.add("/**/*.*");
        //黑名单
        List<String> addPath = new ArrayList();
        registry.addInterceptor(interceptor)
                .excludePathPatterns(excldePath)
                .addPathPatterns(addPath);
    }

}
