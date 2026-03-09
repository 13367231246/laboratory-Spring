package com.lab.pojo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^\\S{5,16}$", message = "用户名需要是5到16个非空白字符")
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "姓名不能为空")
    private String realName;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    @NotBlank(message = "角色不能为空")
    @Pattern(regexp = "^(student|teacher)$", message = "角色只能是student或teacher")
    private String role;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^\\S{5,16}$", message = "密码长度需在5到16个字符之间")
    private String password;

    @NotBlank(message = "学工号不能为空")
    private String studentId;
}

