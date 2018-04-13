package com.noadd.myapp.service.validate.impl;

import com.noadd.myapp.domain.entity.PreValidateCode;
import com.noadd.myapp.domain.mapper.PreValidateCodeMap;
import com.noadd.myapp.mailservice.MyMailService;
import com.noadd.myapp.service.validate.ValidateService;
import com.noadd.myapp.util.baseUtil.StringUtil;
import com.noadd.myapp.util.baseUtil.TimeUtil;
import com.noadd.myapp.util.baseUtil.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateServiceImpl implements ValidateService {
    @Autowired
    PreValidateCodeMap preValidateCodeMap;
    @Autowired
    MyMailService mailService;

    @Override
    public void createValidateCode(String sendType, String sendTo, String codeType) {
        String code = StringUtil.getRandomStr(6);
        String title = "欢迎注册,这是您的注册验证码";
        mailService.sendSimpleMail(sendTo, title, "验证码:" + code + ",请在10分钟内使用");
        PreValidateCode validateCode = new PreValidateCode();
        validateCode.setUuid(UUIDUtil.createUuid());
        validateCode.setCreateTime(TimeUtil.getTimestamp());
        validateCode.setExpiryTime(TimeUtil.stampAdd(validateCode.getCreateTime(), 0, 0, 10, 0));
        validateCode.setCode(code);
        validateCode.setCodeType(codeType);
        validateCode.setSendTo(sendTo);
        validateCode.setSendType(sendType);
        preValidateCodeMap.insertValidateCode(validateCode);
    }
}
