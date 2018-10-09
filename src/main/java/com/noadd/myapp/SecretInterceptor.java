package com.noadd.myapp;

import com.noadd.myapp.domain.entity.PreUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class SecretInterceptor implements HandlerInterceptor {

    @Autowired
    HttpSession session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if (session.getAttribute("loginUser") == null) {
                request.setAttribute("code", "9998");
                request.getRequestDispatcher("/japi/login").forward(request, response);
                return false;
            } else {
                PreUser loginUser = (PreUser) session.getAttribute("loginUser");
                request.setAttribute("userId", loginUser.getUuid());
                request.getRequestDispatcher("/japi/secret/admin/login").forward(request, response);
                return true;
            }
        } catch (Exception e) {
            session = request.getSession(true);
            request.setAttribute("code", "9998");
            request.getRequestDispatcher("/japi/secret/login").forward(request, response);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
