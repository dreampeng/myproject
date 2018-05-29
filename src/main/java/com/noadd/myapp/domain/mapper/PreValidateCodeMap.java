package com.noadd.myapp.domain.mapper;

import com.noadd.myapp.domain.entity.PreValidateCode;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface PreValidateCodeMap {
    /**
     * 生成验证码
     *
     * @param validateCode 验证码
     * @return
     */
    @Insert("insert into pre_validate_code (uuid,send_type,send_to,code,code_type,create_time,expiry_time,is_used) values" +
            " (#{uuid},#{sendType},#{sendTo},#{code},#{codeType},#{createTime},#{expiryTime},#{isUsed})")
    int insertValidateCode(PreValidateCode validateCode);

    /**
     * 获取最新验证码
     *
     * @param sendTo   接收者
     * @param codeType 类型
     * @return
     */
    @Select("select uuid,send_type sendType,send_to sendTo,code,code_type codeType,create_time createTime," +
            "expiry_time expiryTime,is_used isUsed from pre_validate_code where code_type = #{codeType}" +
            " and is_used = 0 and send_to = #{sendTo} order by expiry_time desc limit 1")
    PreValidateCode validateCode(@Param("sendTo") String sendTo, @Param("codeType") String codeType);

    /**
     * 使用验证码
     *
     * @param uuid 唯一标识
     */
    @Update("update pre_validate_code set is_used = 1 where uuid = #{uuid}")
    void updateValidateCode(@Param("uuid") String uuid);
}
