package com.lab.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    @NotBlank(message = "学工号不能为空")
    private String studentId;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^\\S{5,16}$", message = "密码长度需在5到16个字符之间")
    private String password;

    @NotBlank(message = "角色不能为空")
    private String role;
}
