package com.lab.mapper;

import com.lab.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AdminMapper {

    /**
     * 从独立的 admin 表中查询管理员账号
     * 只允许 role 为 admin 或 super_admin
     */
    @Select("select id, username, password, role, last_login_time, last_login_ip, created_at, updated_at " +
            "from admin where username = #{username} and role in ('admin','super_admin')")
    Admin findByUsername(String username);

    /**
     * 更新 admin 表中的管理员密码
     */
    @Update("update admin set password = #{md5String}, updated_at = now() where id = #{id}")
    void updatePassword(Integer id, String md5String);
}