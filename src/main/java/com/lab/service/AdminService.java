package com.lab.service;

import com.lab.pojo.Admin;

public interface AdminService {

    /**
     * 根据用户名查询管理员（来自 admin 表）
     */
    Admin findByUsername(String username);

    /**
     * 修改管理员密码
     */
    void updatePassword(Integer id, String newPwd);
}