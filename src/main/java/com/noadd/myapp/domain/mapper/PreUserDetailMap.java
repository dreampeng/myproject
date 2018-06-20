package com.noadd.myapp.domain.mapper;

import com.noadd.myapp.domain.entity.PreUserDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface PreUserDetailMap {
    /**
     * 根据邮件地址查询是否已注册
     *
     * @param email 邮件地址
     * @return
     */
    @Select("select count(uuid) from pre_user_detail where email = #{email}")
    int getCountByEmail(@Param("email") String email);

    /**
     * 新增用户详情
     *
     * @param userDetail 用户详情
     * @return
     */
    @Insert("insert into pre_user_detail " +
            "(uuid,user_id,nick_name,head_img,email,birth_day,qq,phone,create_by,create_time,update_by,update_time," +
            "is_void) values " +
            "(#{uuid},#{userId},#{nickName},#{headImg},#{email},#{birthDay},#{qq},#{phone},#{createBy},#{createTime}" +
            ",#{updateBy},#{updateTime},#{isVoid})")
    int insertUserDetail(PreUserDetail userDetail);

    @Select("select uuid,user_id userId,nick_name nickName,head_img headImg,email,birth_day birthDay,qq,phone," +
            "create_by createBy,create_time createTime,update_by updateBy,update_time updateTime,is_void isVoid " +
            "from pre_user_detail where user_id = #{userId}")
    PreUserDetail selectUserDetail(@Param("userId") String userId);
}
