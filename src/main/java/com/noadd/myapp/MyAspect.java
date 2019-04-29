package com.noadd.myapp;

import com.noadd.myapp.mailservice.LogToMail;
import com.noadd.myapp.util.MessageUtil;
import com.noadd.myapp.util.securityUtil.SecurityUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;


@Aspect
@Component
public class MyAspect {
    private LogToMail logToMail;
    private HttpServletRequest request;

    @Autowired
    public MyAspect(LogToMail logToMail, HttpServletRequest request) {
        this.logToMail = logToMail;
        this.request = request;
    }

    //所有的contoller的返回参数必须是Map<String,String>
    @Pointcut("execution(public * com.noadd.myapp.controller..*Controller.*(..))")
    public void controller() {
    }


    @Around("controller()")
    public Object aroundController(ProceedingJoinPoint joinPoint) {
        //把加密的参数解密
        Object[] objects = joinPoint.getArgs();
//        for (int x = 0; x < objects.length; x++) {
//            if(objects[x]==null){
//                continue;
//            }
//            if ("String".equals(objects[x].getClass().getSimpleName())) {
//                try {
//                    objects[x] = SecurityUtil.decrypt((String) objects[x]);
//                } catch (Exception e) {
//                    return null;
//                }
//            }
//        }
        Object returnValue;
        Map<String, String> retMap = null;
        try {
            returnValue = joinPoint.proceed(objects);
            retMap = (Map<String, String>) returnValue;
        } catch (Throwable throwable) {
            if (throwable instanceof MailAuthenticationException) {
                retMap = new HashMap<>();
                retMap.put("code", "9800");
            } else {
                Signature s = joinPoint.getSignature();
                MethodSignature ms = (MethodSignature) s;
                Method m = ms.getMethod();
                Parameter[] parameters = m.getParameters();
                StringBuilder methodParam = new StringBuilder();
                StringBuilder paramValue = new StringBuilder();
                for (Parameter temp : parameters) {
                    methodParam.append(", ").append(temp.getType().getSimpleName()).append(" ").append(temp.getName());
                }
                for (Object str : objects) {
                    paramValue.append(", ").append(str);
                }
                String content = "报错信息\n\t方法名:" + m.getName()
                        + "\n\t方法参数(" + ("".equals(methodParam.toString()) ? "" : methodParam.substring(2))
                        + "),\n\t传入参数(" + ("".equals(paramValue.toString()) ? "" : paramValue.substring(2))
                        + ")";
                logToMail.error(content, throwable);
            }
        }
        if (retMap == null) {
            retMap = new HashMap<>();
            retMap.put("code", "9999");
        }
        retMap.put("msg", MessageUtil.sysCodeMsg(retMap.get("code")));
        String[] isFromApp = request.getParameterMap().get("isFromWeb");
        if (isFromApp != null && "1".equals(isFromApp[0])) {
            for (Map.Entry<String, String> temp : retMap.entrySet()) {
                try {
                    retMap.put(temp.getKey(), SecurityUtil.signature(temp.getValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return retMap;
    }
}
