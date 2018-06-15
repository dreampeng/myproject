package com.noadd.myapp.service.validate.impl;

import com.noadd.myapp.domain.entity.PreValidateCode;
import com.noadd.myapp.domain.mapper.PreValidateCodeMap;
import com.noadd.myapp.mailservice.MyMailService;
import com.noadd.myapp.service.validate.ValidateService;
import com.noadd.myapp.util.baseUtil.GUIDUtil;
import com.noadd.myapp.util.baseUtil.StringUtil;
import com.noadd.myapp.util.baseUtil.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateServiceImpl implements ValidateService {
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
        //发送邮件
        mailService.sendSimpleMail(sendTo, title, "验证码:" + code + ",请在10分钟内使用");
        //保存验证码生成信息到数据库
        PreValidateCode validateCode = new PreValidateCode();
        validateCode.setUuid(GUIDUtil.createGuid());
        validateCode.setCreateTime(TimeUtil.getTimestamp());
        validateCode.setExpiryTime(TimeUtil.stampAdd(validateCode.getCreateTime(), 0, 0, 10, 0));
        validateCode.setCode(code);
        validateCode.setCodeType(codeType);
        validateCode.setSendTo(sendTo);
        validateCode.setSendType(sendType);
        validateCode.setIsUsed(0);
        preValidateCodeMap.insertValidateCode(validateCode);
        return result;
    }


    @Override
    public boolean validateCode(String validCode, String sendTo, String codeType) {
        PreValidateCode validateCode = preValidateCodeMap.validateCode(sendTo, codeType);
        if (validateCode == null) {
            return false;
        }
        if (validCode.equals(validateCode.getCode()) && TimeUtil.getTimestamp() < validateCode.getExpiryTime()
                && validateCode.getIsUsed() == 0) {
            preValidateCodeMap.updateValidateCode(validateCode.getUuid());
            return true;
        }
        return false;
    }
}
