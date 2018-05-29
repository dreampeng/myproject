package com.noadd.myapp.service.validate;

public interface ValidateService {
    /**
     * 创建验证码
     *
     * @param sendType
     * @param sendTo
     * @param codeType
     */
    String createValidateCode(String sendType, String sendTo, String codeType);

    /**
     * 验证验证码是否正确
     *
     * @param validCode
     * @return
     */
    boolean validateCode(String validCode, String sendTo, String codeType);
}
