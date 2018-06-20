package com.noadd.myapp.service.user;

import com.noadd.myapp.domain.entity.PreUser;

public interface UserService {
    /**
     * 判断用户名是否已经注册
     *
     * @param userName 用户名
     * @return 是否已注册
     */
    boolean isReg(String userName);

    /**
     * 添加用户
     *
     * @param userName 用户名
     * @param userPass 密码
     * @param email    邮箱
     * @return 注册用户
     */
    PreUser regUser(String userName, String userPass, String email);

    /**
     * 判断邮件是否已经注册
     *
     * @param email 邮件
     * @return 是否已注册
     */
    boolean isRegEmail(String email);

    /**
     * 用户登录
     *
     * @param userName  用户名
     * @param userPass  密码
     * @param validCode 验证码
     * @return 登录结果
     */
    String userLogin(String userName, String userPass, String validCode);
}
