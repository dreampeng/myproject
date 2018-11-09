package com.noadd.myapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class MyWebInterceptor implements HandlerInterceptor {

    @Autowired
    HttpSession session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
//            String strBackUrl = "http://" + request.getServerName() //服务器地址
//                    + ":"
//                    + request.getServerPort()           //端口号
//                    + request.g etContextPath()      //项目名称
//                    + request.getServletPath()      //请求页面或其他地址
//                    + "?" + (request.getQueryString()); //参数
//            request.setAttribute("strBackUrl", strBackUrl);
            if (session.getAttribute("loginUser") == null) {
                request.setAttribute("code", "9998");
                request.getRequestDispatcher("/japi/login").forward(request, response);
                return false;
            }
            return true;
        } catch (Exception e) {
            session = request.getSession(true);
            request.setAttribute("code", "9998");
            request.getRequestDispatcher("/japi/login").forward(request, response);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
