package com.noadd.myapp.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.noadd.myapp.domain.entity.PreUserDetail;
import com.noadd.myapp.service.user.UserService;
import com.noadd.myapp.service.validate.ValidateService;
import com.noadd.myapp.util.ParamUtil;
import com.noadd.myapp.util.baseUtil.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/japi/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    ValidateService validateService;
    @Autowired
    HttpSession session;

    /**
     * 判断用户名是否已注册
     *
     * @param userName
     * @return
     */
    @PostMapping("/isreg")
    public Map<String, String> isReg(String userName) {
        Map<String, String> out = new HashMap<>();
        String code = "0000";
        if (StringUtil.isEmpty(userName)) {
            code = "0001";
        } else {
            if (userService.isReg(userName)) {
                code = "0100";
            }
        }
        out.put("code", code);
        return out;
    }

    /**
     * 判断用户名是否已注册
     *
     * @param email 邮箱
     * @return
     */
    @PostMapping("/isregemail")
    public Map<String, String> isRegEmail(String email) {
        Map<String, String> out = new HashMap<>();
        String code = "0000";
        if (StringUtil.isEmpty(email)) {
            code = "0001";
        } else {
            if (userService.isRegEmail(email)) {
                code = "0101";
            }
        }
        out.put("code", code);
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
    public Map<String, String> regist(String userName, String userPass, String email, String validCode) {
        Map<String, String> out = new HashMap<>();
        String code = "0000";
        if (StringUtil.isEmpty(userName, userPass, email, validCode)) {
            code = "0001";
        } else {
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
        }
        out.put("code", code);
        return out;
    }

    /**
     * 用户登录
     * @param userName
     * @param userPass
     * @param validCode
     * @return
     */
    @PostMapping("/login")
    public Map<String, String> login(String userName, String userPass, String validCode) {
        Map<String, String> out = new HashMap<>();
        String code;
        if (StringUtil.isEmpty(userName, userPass)) {
            code = "0001";
        } else {
            code = userService.userLogin(userName, userPass, validCode);
        }
        out.put("code", code);
        return out;
    }

    /**
     * 获取用户登录详情
     * @return
     */
    @PostMapping("/logindetail")
    public Map<String, Object> getLoginDetail() {
        Map<String, Object> out = new HashMap<>();
        String code = "0000";
        PreUserDetail userDetail = (PreUserDetail) session.getAttribute("loginUser");
        JSONObject jsonObject = new JSONObject();
        if (userDetail != null) {
            jsonObject.put("nickName", userDetail.getNickName());
            jsonObject.put("headImg", userDetail.getHeadImg());
        }
        out.put("code", code);
        out.put("data", jsonObject.toJSONString());
        return out;
    }

}
