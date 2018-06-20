package com.noadd.myapp.domain.mapper;

import com.noadd.myapp.domain.entity.PreUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface PreUserMap {
    /**
     * 根据用户名查用户
     *
     * @param userName
     * @return
     */
    @Select("select uuid,user_name userName,user_pass userPass,login_token loginToken,create_time createTime," +
            "create_by createBy,update_time updateTime,update_by updateBy,is_void isVoid from pre_user " +
            "where user_name = #{userName} and is_void = '0'")
    PreUser getUserByUserName(@Param("userName") String userName);

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @Insert("insert into pre_user (uuid,user_name,user_pass,create_time,create_by,update_time,update_by,is_void," +
            "login_token ) values (#{uuid},#{userName},#{userPass},#{createTime},#{createBy},#{updateTime}," +
            "#{updateBy},#{isVoid},#{loginToken})")
    int insertUser(PreUser user);

    /**
     * 根据用户名查询是否已注册
     *
     * @param userName
     * @return
     */
    @Select("select count(uuid) from pre_user where user_name = #{userName}")
    int getCountByUserName(@Param("userName") String userName);

    /**
     * 用户登录时查询用户
     *
     * @param userName
     * @param userPass
     * @return
     */
    @Select("select uuid,user_name userName,user_pass userPass,login_token loginToken,create_time createTime," +
            "create_by createBy,update_time updateTime,update_by updateBy,is_void isVoid from pre_user " +
            "where user_name = #{userName} and user_pass = #{userPass} and is_void = '0'")
    PreUser userLogin(@Param("userName") String userName, @Param("userPass") String userPass);

    @Update("update pre_user set user_name = #{userName},user_Pass = #{userPass},login_token = #{loginToken}," +
            "create_time = #{createTime},create_by = #{createBy},update_time = #{updateTime},update_by = #{updateBy}," +
            "is_void = #{isVoid} where uuid = #{uuid}")
    int userUpadte(PreUser user);
}
