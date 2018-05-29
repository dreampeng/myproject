package com.noadd.myapp.service.user;

import com.noadd.myapp.domain.entity.PreUser;

public interface UserService {
    /**
     * 判断用户名是否已经注册
     * @param userName 用户名
     * @return
     */
    public boolean isReg(String userName);

    /**
     * 添加用户
     * @param userName 用户名
     * @param userPass 密码
     * @param eamil 邮箱
     * @return
     */
    public PreUser regUser(String userName, String userPass, String eamil);
}
