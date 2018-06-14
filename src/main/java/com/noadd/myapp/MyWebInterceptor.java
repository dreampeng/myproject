package com.noadd.myapp;

import com.noadd.myapp.mailservice.LogToMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

@Component
public class MyWebInterceptor implements HandlerInterceptor {
    @Autowired
    private LogToMail logToMail;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String methodName = handlerMethod.getMethod().getName();
            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
            String methParam = "";
            Map<String, String[]> parameterMap = request.getParameterMap();
            String paramValue = "";
            for (MethodParameter temp : methodParameters) {
                methParam += ", " + temp.getParameterType().getSimpleName() + " " + temp.getParameterName();
                for (Map.Entry<String, String[]> tempParameter : parameterMap.entrySet()) {
                    if (temp.getParameterName().equals(tempParameter.getKey())) {
                        String tempStr = "";
                        for (String str : tempParameter.getValue()) {
                            tempStr += "," + str;
                        }
                        if (tempParameter.getValue().length != 1) {
                            tempStr = "[" + tempStr.substring(1) + "]";
                        } else {
                            tempStr = tempStr.substring(1);
                        }
                        paramValue += "," + tempStr;
                    }
                }
            }
            logToMail.error("方法名:" + methodName +
                    "\n方法参数(" + methParam.substring(2) + ")," +
                    "\n传入参数(" + paramValue.substring(1) + ")", ex);

        }
    }
}
