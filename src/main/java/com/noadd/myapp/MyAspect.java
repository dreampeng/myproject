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
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;


@Aspect
@Component
public class MyAspect {
    @Autowired
    private LogToMail logToMail;
    @Autowired
    HttpServletRequest request;

    //所有的contoller的返回参数必须是Map<String,String>
    @Pointcut("execution(public * com.noadd.myapp.controller..*Controller.*(..))")
    public void controller() {
    }


    @Around("controller()")
    public Object aroundController(ProceedingJoinPoint joinPoint) {
        //把加密的参数解密
        Object[] objects = joinPoint.getArgs();
        for (int x = 0; x < objects.length; x++) {
            if ("String".equals(objects[x].getClass().getSimpleName())) {
                try {
                    objects[x] = SecurityUtil.decrypt((String) objects[x]);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        Object returnValue;
        Map<String, String> retMap = null;
        try {
            returnValue = joinPoint.proceed(objects);
            retMap = (Map<String, String>) returnValue;
        } catch (Throwable throwable) {
            Signature s = joinPoint.getSignature();
            MethodSignature ms = (MethodSignature) s;
            Method m = ms.getMethod();
            Parameter[] parameters = m.getParameters();
            String methodParam = "";
            String paramValue = "";
            for (Parameter temp : parameters) {
                methodParam += ", " + temp.getType().getSimpleName() + " " + temp.getName();
            }
            for (Object str : objects) {
                paramValue += ", " + str;
            }
            logToMail.error("方法名:" + m.getName() +
                    "\n方法参数(" + methodParam.substring(2) + ")," +
                    "\n传入参数(" + paramValue.substring(2) + ")", throwable);
        }
        if (retMap == null) {
            retMap = new HashMap<>();
            retMap.put("code", "9999");
        }
        retMap.put("msg", MessageUtil.sysCodeMsg(retMap.get("code")));
        String[] isFromWeb = request.getParameterMap().get("isFromWeb");
        if (isFromWeb != null && "1".equals(isFromWeb[0])) {
            return retMap;
        }
        for (Map.Entry<String, String> temp : retMap.entrySet()) {
            try {
                retMap.put(temp.getKey(), SecurityUtil.signature(temp.getValue()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return retMap;
    }
}
