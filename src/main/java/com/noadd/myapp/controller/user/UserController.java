package com.noadd.myapp.controller.user;

import com.noadd.myapp.mailservice.LogToMail;
import com.noadd.myapp.service.user.UserService;
import com.noadd.myapp.service.validate.ValidateService;
import com.noadd.myapp.util.MessageUtil;
import com.noadd.myapp.util.ParamUtil;
import com.noadd.myapp.util.baseUtil.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private ValidateService validateService;

    /**
     * 判断用户名是否已注册
     *
     * @param userName
     * @return
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
                code = "9999";
            }
        }
        out.put("code", code);
        out.put("msg", MessageUtil.sysCodeMsg(code));
        return out;
    }

    /**
     * 判断用户名是否已注册
     *
     * @param email 邮箱
     * @return
     */
    @PostMapping("/isregemail")
    public Map<String, Object> isRegEmail(String email) {
        Map<String, Object> out = new HashMap<>();
        String code = "0000";
        if (StringUtil.isEmpty(email)) {
            code = "0001";
        } else {
            try {
                if (userService.isRegEmail(email)) {
                    code = "0101";
                }
            } catch (Exception e) {
                logToMail.error(this.getClass().getName() + "类 isRegEmail(String email)," +
                        "(" + email + ")", e);
                code = "9999";
            }
        }
        out.put("code", code);
        out.put("msg", MessageUtil.sysCodeMsg(code));
        return out;
    }


    /**
     * 用户注册
     *
     * @param userName
     * @param userPass
     * @param email
     * @param validCode
     * @return
     */
    @PostMapping("/regist")
    public Map<String, Object> regist(String userName, String userPass, String email, String validCode) {
        Map<String, Object> out = new HashMap<>();
        String code = "0000";
        if (StringUtil.isEmpty(userName, userPass, email, validCode)) {
            code = "0001";
        } else {
            try {
                if (!ParamUtil.registvalidate(userName, userPass, email)) {
                    code = "0002";
                } else if (userService.isReg(userName)) {
                    code = "0100";
                } else if (userService.isRegEmail(email)) {
                    code = "0101";
                } else if (validateService.validateCode(validCode, email, "0")) {
                    userService.regUser(userName, userPass, email);
                } else {
                    code = "1002";
                }
            } catch (Exception e) {
                logToMail.error(this.getClass().getName() + "类 regist(String userName, String userPass, String email, String validCode)," +
                        "(" + userName + "," + userPass + "," + email + "," + validCode + ")", e);
                code = "9999";
            }
        }
        out.put("code", code);
        out.put("msg", MessageUtil.sysCodeMsg(code));
        return out;
    }

}
