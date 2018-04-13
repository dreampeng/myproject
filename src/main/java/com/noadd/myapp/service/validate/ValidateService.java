package com.noadd.myapp.service.validate;

public interface ValidateService {
    /**
     * 创建验证码
     *
     * @param sendType
     * @param sendTo
     * @param codeType
     */
    void createValidateCode(String sendType, String sendTo, String codeType);
}
