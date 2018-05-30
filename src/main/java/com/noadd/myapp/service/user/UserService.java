package com.noadd.myapp.service.user;

import com.noadd.myapp.domain.entity.PreUser;

public interface UserService {
    /**
     * 判断用户名是否已经注册
     * @param userName 用户名
     * @return
     */
    boolean isReg(String userName);

    /**
     * 添加用户
     * @param userName 用户名
     * @param userPass 密码
     * @param email 邮箱
     * @return
     */
    PreUser regUser(String userName, String userPass, String email);

    /**
     * 判断邮件是否已经注册
     * @param email 邮件
     * @return
     */
    boolean isRegEmail(String email);
}
