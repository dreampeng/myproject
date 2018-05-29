package com.noadd.myapp.service.user.impl;

import com.noadd.myapp.domain.entity.PreUser;
import com.noadd.myapp.domain.mapper.PreUserMap;
import com.noadd.myapp.service.user.UserService;
import com.noadd.myapp.util.baseUtil.TimeUtil;
import com.noadd.myapp.util.baseUtil.GUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PreUserMap userMap;

    @Override
    public boolean isReg(String userName) {
        int count = userMap.getCountByUserName(userName);
        if (count <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public PreUser regUser(String userName, String userPass, String eamil) {
        PreUser preUser = new PreUser();
        preUser.setUuid(GUIDUtil.createGuid());
        preUser.setUserName(userName);
        preUser.setUserPass(userPass);
        preUser.setCreateBy(preUser.getUuid());
        preUser.setCreateTime(TimeUtil.getTimestamp());
        preUser.setIsVoid(0);
        userMap.insertUser(preUser);
        return preUser;
    }
}
