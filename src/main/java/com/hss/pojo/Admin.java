package com.hss.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 管理员实体，对应表 admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    /**
     * 管理员ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（加密存储）
     */
    private String password;

    /**
     * 角色：super_admin / admin
     */
    private String role;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录 IP
     */
    private String lastLoginIp;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}


