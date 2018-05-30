package com.noadd.myapp.service.user.impl;

import com.noadd.myapp.domain.entity.PreUser;
import com.noadd.myapp.domain.entity.PreUserDetail;
import com.noadd.myapp.domain.mapper.PreUserDetailMap;
import com.noadd.myapp.domain.mapper.PreUserMap;
import com.noadd.myapp.service.user.UserService;
import com.noadd.myapp.util.baseUtil.TimeUtil;
import com.noadd.myapp.util.baseUtil.GUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private PreUserMap userMap;
    @Autowired
    private PreUserDetailMap userDetailMap;

    @Override
    public boolean isReg(String userName) {
        int count = userMap.getCountByUserName(userName);
        if (count <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public PreUser regUser(String userName, String userPass, String email) {
        PreUser preUser = new PreUser();
        preUser.setUuid(GUIDUtil.createGuid());
        preUser.setUserName(userName);
        preUser.setUserPass(userPass);
        preUser.setCreateBy(preUser.getUuid());
        preUser.setCreateTime(TimeUtil.getTimestamp());
        preUser.setIsVoid(0);
        userMap.insertUser(preUser);
        PreUserDetail preUserDetail = new PreUserDetail();
        preUserDetail.setUuid(GUIDUtil.createGuid());
        preUserDetail.setEmail(email);
        preUserDetail.setHeadImg("/img/userhead/default.png");
        preUserDetail.setUserId(preUser.getUuid());
        preUserDetail.setCreateBy(preUser.getUuid());
        preUserDetail.setCreateTime(preUser.getCreateTime());
        preUserDetail.setIsVoid(0);
        userDetailMap.insertUserDetail(preUserDetail);
        return preUser;
    }

    @Override
    public boolean isRegEmail(String email) {
        int count = userDetailMap.getCountByEmail(email);
        if (count <= 0) {
            return false;
        }
        return true;
    }
}
