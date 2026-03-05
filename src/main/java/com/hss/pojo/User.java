package com.hss.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 对应数据库表 `user`
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 用户ID，自增主键
     */
    private Integer id;

    /**
     * 用户名，唯一，登录账号
     */
    @Pattern(regexp = "^\\S{5,16}$", message = "用户名需要是5到16个非空白字符")
    private String username;

    /**
     * 登录密码（MD5 等加密后存储）
     */
    @JsonIgnore
    @Pattern(regexp = "^\\S{5,16}$", message = "密码长度需在5到16个字符之间")
    private String password;

    /**
     * 真实姓名（必填）
     */
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    /**
     * 学号/工号，不可空但唯一
     */
    @NotBlank(message = "学号/工号不能为空")
    private String studentId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像 URL
     */
    private String avatar;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    /**
     * 邮箱
     */
    @Email
    private String email;

    /**
     * 角色：admin / teacher / student / visitor
     */
    private String role;

    /**
     * 学院
     */
    private String college;

    /**
     * 专业
     */
    private String major;

    /**
     * 班级
     */
    private String className;

    /**
     * 状态：0-禁用，1-正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

}