package com.noadd.myapp.controller.base;

import com.noadd.myapp.util.MessageUtil;
import com.noadd.myapp.util.baseUtil.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/regist")
    public Map<String, Object> login(String userName, String userPass, String email, String validCode) throws InterruptedException {
        Map<String, Object> out = new HashMap<>();
        String code = "0000";
        if (StringUtil.isEmpty(userName, userPass, email, validCode)) {
            code = "0001";
        } else {
            try {

            } catch (Exception e) {
                logger.error("注册失败," +
                        "\n参数(String userName, String userPass, String email, String validCode)," +
                        "\n(" + userName + "," + userPass + "," + email + "," + validCode + ")", e);
                code = "1001";
            }
        }
        out.put("code", code);
        out.put("msg", MessageUtil.sysCodeMsg(code));
        return out;
    }
}
