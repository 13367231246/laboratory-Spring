package com.lab.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 对应数据库表 document（协议/手册/规则）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    /**
     * 文档ID
     */
    private Integer id;

    /**
     * 文档类型：protocol/manual/rule/other
     */
    private String docType;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 版本号
     */
    private String version;

    /**
     * 发布管理员ID（关联 admin 表）
     */
    private Integer adminId;

    /**
     * 状态：0-草稿，1-已发布
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}


