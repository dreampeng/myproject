package com.noadd.myapp.controller.secret;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("secret")
public class UserController {
    @PostMapping("/login")
    public Map<String, Object> login(String userName, String userPass) {
        return null;
    }
}
