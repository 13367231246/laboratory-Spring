package com.lab.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 对应数据库表 lab_schedule（实验室开放时间配置表）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabSchedule {

    /**
     * 配置ID
     */
    private Integer id;

    /**
     * 规则名称（如：春季学期、国庆节等）
     */
    private String ruleName;

    /**
     * 规则类型（学期时间、节假日、常规时间等）
     */
    private String ruleType;

    /**
     * 适用实验室ID，多个用逗号分隔，all表示所有实验室
     */
    private String laboratoryIds;

    /**
     * 实验室名称（冗余字段，多个用逗号分隔）
     */
    private String laboratoryNames;

    /**
     * 时间段，JSON格式存储
     * 示例：[{"start":"08:00","end":"10:00"},{"start":"10:00","end":"12:00"}]
     */
    private String timeSlots;

    /**
     * 有效期开始日期
     */
    private LocalDate startDate;

    /**
     * 有效期结束日期
     */
    private LocalDate endDate;

    /**
     * 适用星期(1-7，逗号分隔，如：1,2,3,4,5表示周一到周五)
     */
    private String weekdays;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}


