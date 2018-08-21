package com.noadd.myapp.controller.secret;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("secret/admin")
public class SAdminController {
    @Autowired
    HttpSession session;

    @RequestMapping("/login")
    public Map<String, Object> adminLogin(String userId) {
        session.setAttribute("key",null);
        return null;
    }
}
