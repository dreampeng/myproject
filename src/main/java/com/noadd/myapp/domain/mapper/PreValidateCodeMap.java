package com.noadd.myapp.domain.mapper;

import com.noadd.myapp.domain.entity.PreValidateCode;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface PreValidateCodeMap {
    @Insert("insert into pre_validate_code (uuid,send_type,send_to,code,code_type,create_time,expiry_time) values" +
            " (#{uuid},#{sendType},#{sendTo},#{code},#{codeType},#{createTime},#{expiryTime})")
    int insertValidateCode(PreValidateCode validateCode);
}
