package com.lab.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 对应数据库表 notice（公告表）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {

    /**
     * 公告ID
     */
    private Integer id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 发布人ID（关联 admin 表）
     */
    private Integer publisherId;

    /**
     * 发布人姓名（冗余字段）
     */
    private String publisherName;

    /**
     * 公告类型：system/lab/academic/other
     */
    private String type;

    /**
     * 优先级：1-普通，2-重要，3-紧急
     */
    private Integer priority;

    /**
     * 状态：0-草稿，1-已发布，2-已下线
     */
    private Integer status;

    /**
     * 浏览次数
     */
    private Integer views;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 过期时间（null表示永不过期）
     */
    private LocalDateTime expireTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

