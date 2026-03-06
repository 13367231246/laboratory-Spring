package com.hss.controller;

import com.hss.pojo.PageBean;
import com.hss.pojo.Result;
import com.hss.pojo.User;
import com.hss.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-management")
@Validated
public class UserManagementController {

    @Autowired
    private UserManagementService userManagementService;

    /**
     * 分页获取学生列表（管理员）
     */
    @GetMapping("/students")
    public Result<PageBean<User>> listStudents(@RequestParam Integer pageNo,
                                               @RequestParam Integer pageSize) {
        PageBean<User> pageBean = userManagementService.listStudents(pageNo, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 分页获取老师列表（管理员）
     */
    @GetMapping("/teachers")
    public Result<PageBean<User>> listTeachers(@RequestParam Integer pageNo,
                                               @RequestParam Integer pageSize) {
        PageBean<User> pageBean = userManagementService.listTeachers(pageNo, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 根据ID获取用户信息（管理员）
     */
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Integer id) {
        User user = userManagementService.getById(id);
        return Result.success(user);
    }

    /**
     * 新增学生（管理员）
     */
    @PostMapping("/student")
    public Result addStudent(@RequestBody User user) {
        userManagementService.addStudent(user);
        return Result.success();
    }

    /**
     * 新增老师（管理员）
     */
    @PostMapping("/teacher")
    public Result addTeacher(@RequestBody User user) {
        userManagementService.addTeacher(user);
        return Result.success();
    }

    /**
     * 更新用户信息（不能编辑用户名，管理员）
     */
    @PutMapping
    public Result update(@RequestBody User user) {
        userManagementService.update(user);
        return Result.success();
    }

    /**
     * 重置密码（一键重置为123456，管理员）
     */
    @PatchMapping("/{id}/reset-password")
    public Result resetPassword(@PathVariable Integer id) {
        userManagementService.resetPassword(id);
        return Result.success("密码已重置为123456");
    }

    /**
     * 禁用用户（管理员）
     */
    @PatchMapping("/{id}/disable")
    public Result disable(@PathVariable Integer id) {
        userManagementService.disable(id);
        return Result.success();
    }

    /**
     * 启用用户（管理员）
     */
    @PatchMapping("/{id}/enable")
    public Result enable(@PathVariable Integer id) {
        userManagementService.enable(id);
        return Result.success();
    }

    /**
     * 删除用户（管理员）
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        userManagementService.delete(id);
        return Result.success();
    }
}

