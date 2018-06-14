package com.noadd.myapp;

import com.noadd.myapp.mailservice.LogToMail;
import com.noadd.myapp.util.securityUtil.SecurityUtil;
import org.apache.catalina.util.ParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Component
public class MyWebInterceptor implements HandlerInterceptor {
    @Autowired
    private LogToMail logToMail;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (Map.Entry<String, String[]> temp : parameterMap.entrySet()) {
                for (int x = 0; x < temp.getValue().length; x++) {
                    request.setAttribute(temp.getKey(), SecurityUtil.decrypt(temp.getValue()[x]));
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String methodName = handlerMethod.getMethod().getName();
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        String methParam = "";
        for (MethodParameter temp : methodParameters) {
            methParam += ", " + temp.getParameterType().getSimpleName() + " " + temp.getParameterName();
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (ex != null) {
            logToMail.error("方法名:" + methodName +
                    "\n参数(" + methParam.substring(2) + "),", ex);
//                    +
//                    "\n(" + sendType + "," + sendTo + "," + codeType + ")", ex);

        }
        System.out.println();
    }
}
