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
    @Resource
    private SecretInterceptor secretInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //----------------前台拦截----------------
        //白名单
        List<String> excldePath = new ArrayList();
        //错误界面
        excldePath.add("/error");
        //跳转到登陆页面
        excldePath.add("/login");
        //获取验证码
        excldePath.add("/validate/getcode");
        //用户相关
        excldePath.add("/user/isreg");
        excldePath.add("/user/isregemail");
        excldePath.add("/user/regist");
        excldePath.add("/user/login");
//        excldePath.add("/user/logindetail");
        //过滤所有静态文件
        excldePath.add("/**/*.*");
        //黑名单
        List<String> addPath = new ArrayList();
        registry.addInterceptor(interceptor)
                .excludePathPatterns(excldePath)
                .addPathPatterns(addPath);

        //----------------私密拦截----------------
        //白名单
        List<String> secretExcldePath = new ArrayList();
        //错误界面
        secretExcldePath.add("/error");
        //跳转到登陆页面
        secretExcldePath.add("/secret/login");
        //过滤所有静态文件
        excldePath.add("/**/*.*");
        //黑名单
        List<String> secretAddPath = new ArrayList();
        secretAddPath.add("/secret/**/*");
        secretAddPath.add("/secret/**/*.html");
        registry.addInterceptor(secretInterceptor)
                .addPathPatterns(secretAddPath)
                .excludePathPatterns(secretExcldePath);

    }

}
