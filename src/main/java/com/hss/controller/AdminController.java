package com.hss.controller;

import com.hss.pojo.Admin;
import com.hss.pojo.AdminLoginRequest;
import com.hss.pojo.Result;
import com.hss.service.AdminService;
import com.hss.utils.JwtUtil;
import com.hss.utils.Md5Util;
import com.hss.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/admin")
@Validated
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 管理员登录（用户名 + 密码，role 固定为 admin）
    @PostMapping("/login")
public Result adminLogin(@Valid @RequestBody AdminLoginRequest request) {
    String username = request.getUsername();
    String password = request.getPassword();
        String role = "admin";
        // 根据用户名查询管理员
        Admin admin = adminService.findByUsername(username);
        if (admin == null) {
        return Result.error("用户名或密码错误");
    }
    // 校验密码（MD5）
        if (!Md5Util.getMD5String(password).equals(admin.getPassword())) {
        return Result.error("用户名或密码错误");
    }

    // 登录成功，生成 token
    Map<String, Object> claims = new HashMap<>();
        claims.put("id", admin.getId());
        claims.put("username", admin.getUsername());
        claims.put("role", role);
    String token = JwtUtil.genToken(claims);

    // 存入 redis
    ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
    operations.set(token, token, 1, TimeUnit.HOURS);

    return Result.success(token);
}

// 管理员修改密码
    @PatchMapping("/updatePwd")
public Result adminUpdatePassword(@RequestBody Map<String, String> params,
                                  @RequestHeader("Authorization") String token) {
        String oldPwd = params.get("oldPassword");
        String newPwd = params.get("newPassword");
        String rePwd  = params.get("confirmPassword");

        if (!StringUtils.hasLength(oldPwd) ||
                !StringUtils.hasLength(newPwd) ||
                !StringUtils.hasLength(rePwd)) {
        return Result.error("缺少必要的参数");
    }
    if (newPwd.length() < 5 || newPwd.length() > 16) {
        return Result.error("新密码长度需在5到16个字符之间");
    }

        // 从 ThreadLocal 拿当前登录用户（由拦截器根据 token 写入）
    Map<String, Object> map = ThreadLocalUtil.get();
    String username = (String) map.get("username");

        // 根据用户名查询管理员
        Admin admin = adminService.findByUsername(username);
        if (admin == null) {
        return Result.error("用户不存在");
    }

    // 校验旧密码
        if (!admin.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
        return Result.error("原密码填写不正确");
    }

    // 校验两次新密码一致
    if (!newPwd.equals(rePwd)) {
        return Result.error("密码不一致");
    }

        // 修改密码（通过 AdminService）
        adminService.updatePassword(admin.getId(), newPwd);

    // 使当前 token 失效
    ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
    operations.getOperations().delete(token);

        return Result.success(1);
    }

    // 获取管理员详细信息（返回 Admin，与 admin 表结构对应）
    @GetMapping("/adminInfo")
    public Result<Admin> adminInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");

        Admin admin = adminService.findByUsername(username);
        return Result.success(admin);
}
}