package com.lab.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 对应数据库表 lab_application（实验室申请表）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabApplication {

    /** 申请ID */
    private Integer id;
    /** 申请单号 */
    private String applicationNo;
    /** 申请的实验室ID */
    private Integer laboratoryId;
    /** 实验室房间号（冗余字段） */
    private String labNumber;
    /** 实验室名称（冗余字段） */
    private String labName;
    /** 实验室具体位置（冗余字段） */
    private String location;
    /** 申请人ID */
    private Integer applicantId;
    /** 申请人角色：student / teacher */
    private String applicantRole;
    /** 使用目的(个人使用/课程使用) */
    private String purpose;
    /** 课程名称(如果课程使用) */
    private String courseName;
    /** 班级名称 */
    private String className;
    /** 学生人数 */
    private Integer studentCount;
    /** 开始使用时间 */
    private LocalDateTime startTime;
    /** 结束使用时间 */
    private LocalDateTime endTime;
    /** 状态：0-待审核，1-已批准，2-已拒绝，3-使用中，4-已完成，5-已取消 */
    private Integer status;
    /** 审核人ID */
    private Integer reviewerId;
    /** 审核时间 */
    private LocalDateTime reviewTime;
    /** 审核意见 */
    private String reviewComment;
    /** 实际完成时间 */
    private LocalDateTime completionTime;
    /** 申请时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
