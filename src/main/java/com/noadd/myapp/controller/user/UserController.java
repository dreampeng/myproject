package com.noadd.myapp.controller.user;

import com.noadd.myapp.mailservice.LogToMail;
import com.noadd.myapp.service.user.UserService;
import com.noadd.myapp.util.MessageUtil;
import com.noadd.myapp.util.baseUtil.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private LogToMail logToMail;
    @Autowired
    private UserService userService;

    /**
     * 判断用户名是否已注册
     *
     * @param userName
     * @return
     * @throws InterruptedException
     */
    @PostMapping("/isreg")
    public Map<String, Object> isReg(String userName) {
        Map<String, Object> out = new HashMap<>();
        String code = "0000";
        if (StringUtil.isEmpty(userName)) {
            code = "0001";
        } else {
            try {
                if (userService.isReg(userName)) {
                    code = "0100";
                }
            } catch (Exception e) {
                logToMail.error(this.getClass().getName() + "类 isReg(String userName)," +
                        "(" + userName + ")", e);
                code = "1001";
            }
        }
        out.put("code", code);
        out.put("msg", MessageUtil.sysCodeMsg(code));
        return out;
    }


    @PostMapping("/regist")
    public Map<String, Object> login(String userName, String userPass, String email, String validCode) {
        Map<String, Object> out = new HashMap<>();
        String code = "0000";
        if (StringUtil.isEmpty(userName, userPass, email, validCode)) {
            code = "0001";
        } else {
            try {

            } catch (Exception e) {
                logToMail.error(this.getClass().getName() + "类 login(String userName, String userPass, String email, String validCode)," +
                        "(" + userName + "," + userPass + "," + email + "," + validCode + ")", e);
                code = "1001";
            }
        }
        out.put("code", code);
        out.put("msg", MessageUtil.sysCodeMsg(code));
        return out;
    }

}
