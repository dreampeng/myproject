package com.noadd.myapp.domain.mapper.secret;

import com.noadd.myapp.domain.entity.secret.PreSecretAdmin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface PreSecretAdminMap {
    @Select("select uuid,user_id userId,admin_type adminType,balance,isVoid,adminStatus form " +
            "pre_secret_admin where user_id = #{userId}")
    PreSecretAdmin getAdminByUserId(@Param("userId") String userId);
}
