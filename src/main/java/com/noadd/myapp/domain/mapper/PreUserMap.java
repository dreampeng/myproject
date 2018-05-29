package com.noadd.myapp.domain.mapper;

import com.noadd.myapp.domain.entity.PreUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface PreUserMap {
    /**
     * 根据用户名查用户
     * @param userName
     * @return
     */
    @Select("select uuid,user_name userName,user_pass userPass,create_time createTime,create_by createBy,update_time " +
            "updateTime,update_by updateBy,is_void isVoid from pre_user where user_name = #{userName} and is_void = '0'")
    PreUser getUserByUserName(@Param("userName") String userName);

    /**
     * 添加用户
     * @param user
     * @return
     */
    @Insert("insert into pre_user (uuid,user_name,user_pass,create_time,create_by,update_time,update_by,is_void ) values" +
            " (#{uuid},#{userName},#{userPass},#{createTime},#{createBy},#{updateTime},#{updateBy},#{isVoid})")
    int insertUser(PreUser user);

    /**
     * 根据用户名查询是否已注册
     * @param userName
     * @return
     */
    @Select("select count(uuid) from pre_user where user_name = #{userName}")
    int getCountByUserName(@Param("userName") String userName);
}
