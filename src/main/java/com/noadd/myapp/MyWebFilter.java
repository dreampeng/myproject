package com.noadd.myapp;

import com.noadd.myapp.util.securityUtil.SecurityUtil;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyWebFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        try {
            Map<String, String[]> parameterMap = httpRequest.getParameterMap();
            for (Map.Entry<String, String[]> temp : parameterMap.entrySet()) {
                for (int x = 0; x < temp.getValue().length; x++) {
                    temp.getValue()[x] = SecurityUtil.decrypt(temp.getValue()[x]);
                }
            }
        } catch (Exception e) {
        }
        Map<String, String[]> m = new HashMap<>(httpRequest.getParameterMap());
        httpRequest = new RequestWrapper(httpRequest, m);
        filterChain.doFilter(httpRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
