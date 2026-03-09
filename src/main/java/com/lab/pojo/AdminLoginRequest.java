package com.lab.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdminLoginRequest {

    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^\\S{5,16}$", message = "用户名需要是5到16个非空白字符")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^\\S{5,16}$", message = "密码长度需在5到16个字符之间")
    private String password;
}