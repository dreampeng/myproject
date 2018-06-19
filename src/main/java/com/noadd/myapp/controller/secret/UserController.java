package com.noadd.myapp.controller.secret;

import com.noadd.myapp.service.secret.SUerService;
import com.noadd.myapp.util.baseUtil.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("secret")
public class UserController {
    @Autowired
    private SUerService sUerService;

    @PostMapping("/login")
    public Map<String, String> login(String userName, String userPass) {
        Map<String, String> out = new HashMap<>();
        String code = "0000";
        if (StringUtil.isEmpty(userName, userPass)) {
            code = "0001";
        } else {

        }
        out.put("code", code);
        return out;
    }
}
