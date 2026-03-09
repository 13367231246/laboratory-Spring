package com.lab.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 维护记录实体类
 * 对应数据库表：maintenance_record
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRecord {
    
    /**
     * 维修ID，主键自增
     */
    private Integer id;
    
    /**
     * 维修单号，唯一索引
     */
    private String recordNo;
    
    /**
     * 实验室ID
     */
    private Integer laboratoryId;
    
    /**
     * 设备ID
     */
    private Integer equipmentId;
    
    /**
     * 报修人ID
     */
    private Integer reporterId;
    
    /**
     * 报修人角色：student-学生，teacher-教师，admin-管理员
     */
    private String reporterRole;
    
    /**
     * 问题类型：clean-清洁，repair-维修，accident-事故，other-其他
     */
    private String issueType;
    
    /**
     * 问题描述
     */
    private String description;
    
    /**
     * 问题照片（多个URL用逗号分隔）
     */
    private String photos;
    
    /**
     * 状态：0-等待维修，1-维修中，2-已完成维修
     */
    private Integer status;
    
    /**
     * 指派的教师ID
     */
    private Integer assignedTeacherId;
    
    /**
     * 维修开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime repairStartTime;
    
    /**
     * 维修完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime repairEndTime;
    
    /**
     * 维修结果
     */
    private String repairResult;
    
    /**
     * 维修费用
     */
    private BigDecimal cost;
    
    /**
     * 报修时间（创建时间）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}