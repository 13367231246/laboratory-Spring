package com.hss.mapper;

import com.hss.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    // 根据用户名，查询用户，返回User对象
    @Select("select * from user where username=#{username}")
    User findUserName(String username);

    // 根据手机号和学工号同时查询用户（用于登录）
    @Select("select * from user where phone=#{phone} and student_id=#{studentId}")
    User findByPhoneAndStudentId(String phone, String studentId);

    // 根据手机号或学工号查询用户（用于注册时检查）
    @Select("select * from user where phone=#{identifier} or student_id=#{identifier}")
    User findByIdentifier(String identifier);

    // 根据手机号查询用户
    @Select("select * from user where phone=#{phone}")
    User findByPhone(String phone);

    // 根据学工号查询用户
    @Select("select * from user where student_id=#{studentId}")
    User findByStudentId(String studentId);

    // 写入数据库（注册用户，必填字段：username、password、real_name、email、phone、role）
    @Insert("insert into user(username,password,real_name,student_id,email,phone,role,status,create_time,update_time) " +
            "values(#{username},#{password},#{realName},#{studentId},#{email},#{phone},#{role},#{status},now(),now())")
    void add(User user);

    // 更新用户基础信息（不包含密码）
    @Update("update user set real_name=#{realName},student_id=#{studentId},nickname=#{nickname}," +
            "avatar=#{avatar},phone=#{phone},email=#{email},college=#{college},major=#{major}," +
            "class_name=#{className},status=#{status},update_time=now() where id=#{id}")
    void updateInfo(User user);

    // 更新用户头像
    @Update("update user set avatar=#{avatarUrl},update_time=now() where id=#{id}")
    void updateAvatar(Integer id, String avatarUrl);

    // 更新用户密码
    @Update("update user set password=#{md5String},update_time=now() where id=#{id}")
    void updatePassword(Integer id, String md5String);

    // 删除用户
    @Delete("delete from user where id=#{id}")
    void deleteUser(Integer id);
}
