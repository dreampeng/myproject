package com.noadd.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PageController {
    @Autowired
    HttpServletRequest request;

    @RequestMapping("login")
    public Map<String, String> toLogin() {
        Map<String, String> out = new HashMap<>();
        String code = (String) request.getAttribute("code");
        out.put("code", code);
        return out;
    }

    @RequestMapping("/secret/login")
    public Map<String, String> toSecretLogin() {
        Map<String, String> out = new HashMap<>();
        String code = (String) request.getAttribute("code");
        out.put("code", code);
        return out;
    }
}
