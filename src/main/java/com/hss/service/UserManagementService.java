package com.hss.service;

import com.hss.pojo.PageBean;
import com.hss.pojo.User;

public interface UserManagementService {

    /**
     * 分页获取学生列表
     */
    PageBean<User> listStudents(Integer pageNo, Integer pageSize);

    /**
     * 分页获取老师列表
     */
    PageBean<User> listTeachers(Integer pageNo, Integer pageSize);

    /**
     * 根据ID获取用户信息
     */
    User getById(Integer id);

    /**
     * 新增学生
     */
    void addStudent(User user);

    /**
     * 新增老师
     */
    void addTeacher(User user);

    /**
     * 更新用户信息（不能编辑用户名）
     */
    void update(User user);

    /**
     * 重置密码（一键重置为123456）
     */
    void resetPassword(Integer id);

    /**
     * 禁用用户
     */
    void disable(Integer id);

    /**
     * 启用用户
     */
    void enable(Integer id);

    /**
     * 删除用户
     */
    void delete(Integer id);
}

