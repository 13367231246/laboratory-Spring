package com.lab.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 设备申请实体类
 * 对应数据库表：equipment_application
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentApplication {
    
    /**
     * 申请ID，主键自增
     */
    private Integer id;
    
    /**
     * 申请单号，唯一索引
     */
    private String applicationNo;
    
    /**
     * 申请的设备ID，外键关联equipment表
     */
    private Integer equipmentId;

    private String equipmentName;

    private String equipmentType;

    private String equipmentModel;

    private String assetNumber;
    
    /**
     * 所在实验室ID，外键关联laboratory表
     */
    private Integer laboratoryId;

    private String labNumber;

    private String labName;

    private String location;
    
    /**
     * 申请人ID，外键关联user表
     */
    private Integer applicantId;
    
    /**
     * 申请人角色：student-学生，teacher-教师
     */
    private String applicantRole;
    
    /**
     * 申请人真实姓名（冗余字段）
     */
    private String applicantRealName;
    
    /**
     * 申请人联系电话
     */
    private String applicantPhone;
    
    /**
     * 申请人邮箱
     */
    private String applicantEmail;
    
    /**
     * 申请数量，默认为1
     */
    private Integer quantity;
    
    /**
     * 使用目的
     */
    private String purpose;
    
    /**
     * 开始使用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    /**
     * 结束使用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    /**
     * 状态：0-待审核，1-已批准，2-已拒绝，3-使用中，4-已完成，5-已取消
     */
    private Integer status;
    
    /**
     * 审核人ID(教师/管理员)，外键关联user表
     */
    private Integer reviewerId;

    private String reviewerName;

    private String reviewerRole;
    
    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewTime;
    
    /**
     * 审核意见
     */
    private String reviewComment;
    
    /**
     * 实际完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completionTime;
    
    /**
     * 创建时间（申请时间）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}