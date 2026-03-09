package com.lab.mapper;

import com.lab.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserManagementMapper {

    // 分页查询学生列表
    @Select("select * from user where role = 'student' order by create_time desc limit #{offset}, #{pageSize}")
    List<User> findStudentsPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    // 统计学生总数
    @Select("select count(*) from user where role = 'student'")
    Long countStudents();

    // 分页查询老师列表
    @Select("select * from user where role = 'teacher' order by create_time desc limit #{offset}, #{pageSize}")
    List<User> findTeachersPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    // 统计老师总数
    @Select("select count(*) from user where role = 'teacher'")
    Long countTeachers();

    // 根据ID查询用户
    @Select("select * from user where id = #{id}")
    User findById(Integer id);

    // 根据学工号查询用户（用于检查是否重复）
    @Select("select * from user where student_id = #{studentId}")
    User findByStudentId(String studentId);

    // 根据用户名查询用户（用于检查是否重复）
    @Select("select * from user where username = #{username}")
    User findByUsername(String username);

    // 根据手机号查询用户（用于检查是否重复）
    @Select("select * from user where phone = #{phone}")
    User findByPhone(String phone);

    // 新增用户（学生或老师）
    @Insert("insert into user(username, password, real_name, student_id, phone, email, role, " +
            "college, major, class_name, title, department, status, create_time, update_time) " +
            "values(#{username}, #{password}, #{realName}, #{studentId}, #{phone}, #{email}, #{role}, " +
            "#{college}, #{major}, #{className}, #{title}, #{department}, #{status}, now(), now())")
    void add(User user);

    // 更新用户信息（不包含用户名和密码）
    @Update("update user set real_name=#{realName}, student_id=#{studentId}, phone=#{phone}, " +
            "email=#{email}, college=#{college}, major=#{major}, class_name=#{className}, " +
            "title=#{title}, department=#{department}, status=#{status}, update_time=now() where id=#{id}")
    void update(User user);

    // 重置密码
    @Update("update user set password=#{md5String}, update_time=now() where id=#{id}")
    void resetPassword(@Param("id") Integer id, @Param("md5String") String md5String);

    // 修改状态（禁用/启用）
    @Update("update user set status=#{status}, update_time=now() where id=#{id}")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    // 删除用户
    @Delete("delete from user where id = #{id}")
    void delete(Integer id);
}

