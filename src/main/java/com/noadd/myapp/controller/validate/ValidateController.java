package com.noadd.myapp.controller.validate;

import com.noadd.myapp.mailservice.LogToMail;
import com.noadd.myapp.service.user.UserService;
import com.noadd.myapp.service.validate.ValidateService;
import com.noadd.myapp.util.MessageUtil;
import com.noadd.myapp.util.RegularUtil;
import com.noadd.myapp.util.baseUtil.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private LogToMail logToMail;
    @Autowired
    private ValidateService validateService;
    @Autowired
    private UserService userService;

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
        Map<String, Object> out = new HashMap<>();
        String code = "0000";
        if (StringUtil.isEmpty(sendType, sendTo, codeType)) {
            code = "0001";
        } else if (RegularUtil.regularMatch(sendTo, RegularUtil.email)) {
            code = "0002";
        } else {
            try {
                if ("0".equals(codeType) && userService.isRegEmail(sendTo)) {
                    code = "0101";
                } else {
                    validateService.createValidateCode(sendType, sendTo, codeType);
                }
            } catch (Exception e) {
                logToMail.error("验证码获取失败," +
                        "\n参数(String sendType, String sendTo, String codeType)," +
                        "\n(" + sendType + "," + sendTo + "," + codeType + ")", e);
                code = "9999";
            }
        }
        out.put("code", code);
        out.put("msg", MessageUtil.sysCodeMsg(code));
        return out;
    }
}
