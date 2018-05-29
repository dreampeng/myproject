package com.noadd.myapp.controller.validate;

import com.noadd.myapp.mailservice.LogToMail;
import com.noadd.myapp.service.validate.ValidateService;
import com.noadd.myapp.util.MessageUtil;
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
    ValidateService validateService;

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
        } else {
            try {
                validateService.createValidateCode(sendType, sendTo, codeType);
            } catch (Exception e) {
                logToMail.error("验证码获取失败," +
                        "\n参数(String sendType, String sendTo, String codeType)," +
                        "\n(" + sendType + "," + sendTo + "," + codeType + ")", e);
                code = "1001";
            }
        }
        out.put("code", code);
        out.put("msg", MessageUtil.sysCodeMsg(code));
        return out;
    }
}
