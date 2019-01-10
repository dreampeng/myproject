package com.noadd.myapp.controller;

import com.noadd.myapp.redis.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/japi")
public class PageController {
    @Autowired
    HttpServletRequest request;

    @RequestMapping("/login")
    public Map<String, String> toLogin() {
        Map<String, String> out = new HashMap<>();
        String code = (String) request.getAttribute("code");
        out.put("code", code);
//        String strBackUrl = (String) request.getAttribute("strBackUrl");
//        out.put("strBackUrl", strBackUrl);
        return out;
    }

    @RequestMapping("/secret/login")
    public Map<String, String> toSecretLogin() {
        Map<String, String> out = new HashMap<>();
        String code = (String) request.getAttribute("code");
        out.put("code", code);
        return out;
    }

    @Autowired
    RedisManager redisManager;

    @RequestMapping("/test/put")
    public Map<String, String> testRedis(String key, String value) {
        Map<String, String> out = new HashMap<>();
        out.put("code", "0000");
        redisManager.update(key, value);
        return out;
    }

    @RequestMapping("/test/get")
    public Map<String, String> testRedis(String key) {
        Map<String, String> out = new HashMap<>();
        out.put("code", "0000");
        out.put("data", (String) redisManager.select(key));
        return out;
    }

}
