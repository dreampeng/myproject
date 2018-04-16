package com.noadd.myapp.service.validate.impl;

import com.noadd.myapp.domain.entity.PreValidateCode;
import com.noadd.myapp.domain.mapper.PreValidateCodeMap;
import com.noadd.myapp.mailservice.MyMailService;
import com.noadd.myapp.service.validate.ValidateService;
import com.noadd.myapp.util.baseUtil.StringUtil;
import com.noadd.myapp.util.baseUtil.TimeUtil;
import com.noadd.myapp.util.baseUtil.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class ValidateServiceImpl implements ValidateService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    PreValidateCodeMap preValidateCodeMap;
    @Autowired
    MyMailService mailService;

    @Override
    public String createValidateCode(String sendType, String sendTo, String codeType) {
        //生成验证码
        String code = StringUtil.getRandomStr(6).toUpperCase();
        String title = "欢迎注册,这是您的注册验证码";
        String result = "1";
        try {
            //发送邮件
            mailService.sendSimpleMail(sendTo, title, "验证码:" + code + ",请在10分钟内使用");
            //保存验证码生成信息到数据库
            PreValidateCode validateCode = new PreValidateCode();
            validateCode.setUuid(UUIDUtil.createUuid());
            validateCode.setCreateTime(TimeUtil.getTimestamp());
            validateCode.setExpiryTime(TimeUtil.stampAdd(validateCode.getCreateTime(), 0, 0, 10, 0));
            validateCode.setCode(code);
            validateCode.setCodeType(codeType);
            validateCode.setSendTo(sendTo);
            validateCode.setSendType(sendType);
            preValidateCodeMap.insertValidateCode(validateCode);
        } catch (MessagingException e) {
            logger.error("邮件发送失败," +
                    "\n参数(String sendType, String sendTo, String codeType)," +
                    "\n(" + sendType + "," + sendTo + "," + codeType + ")", e);
            result = "-1";
        }
        return result;
    }
}
