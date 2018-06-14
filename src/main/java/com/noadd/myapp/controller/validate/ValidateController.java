package com.noadd.myapp.controller.validate;

import com.noadd.myapp.mailservice.LogToMail;
import com.noadd.myapp.service.user.UserService;
import com.noadd.myapp.service.validate.ValidateService;
import com.noadd.myapp.util.MessageUtil;
import com.noadd.myapp.util.RegularUtil;
import com.noadd.myapp.util.baseUtil.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码相关接口
 */
@RestController
@RequestMapping("/validate")
@Transactional
public class ValidateController {
    @Autowired
    private ValidateService validateService;
    @Autowired
    private UserService userService;
    @Autowired
    HttpServletRequest request;

    /**
     * 获取验证码
     *
     * @param sendType
     * @param sendTo
     * @param codeType
     * @return
     * @throws InterruptedException
     */
    @PostMapping("/getcode")
    public Map<String, Object> login(String sendType, String sendTo, String codeType) throws InterruptedException {
        sendType = (String) request.getAttribute("sendType");
        sendTo = (String) request.getAttribute("sendTo");
        codeType = (String) request.getAttribute("codeType");
        Map<String, Object> out = new HashMap<>();
        String code = "0000";
        if (StringUtil.isEmpty(sendType, sendTo, codeType)) {
            code = "0001";
        } else if (RegularUtil.regularMatch(sendTo, RegularUtil.email)) {
            code = "0002";
        } else {
            if ("0".equals(codeType) && userService.isRegEmail(sendTo)) {
                code = "0101";
            } else {
                validateService.createValidateCode(sendType, sendTo, codeType);
            }
        }
        out.put("code", code);
        out.put("msg", MessageUtil.sysCodeMsg(code));
        return out;
    }
}
