package com.hss.service;

import com.hss.pojo.Admin;

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