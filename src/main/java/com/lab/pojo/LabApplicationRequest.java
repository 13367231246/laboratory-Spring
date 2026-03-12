package com.lab.pojo;

import lombok.Data;

/**
 * 实验室申请提交的请求体（前端传入）
 * startTime / endTime 使用 HH:mm:ss 格式的时间字符串
 */
@Data
public class LabApplicationRequest {

    /** 申请的实验室ID */
    private Integer laboratoryId;

    /** 使用目的(个人使用/课程使用) */
    private String purpose;

    /** 课程名称(如果课程使用) */
    private String courseName;

    /** 班级名称 */
    private String className;

    /** 学生人数 */
    private Integer studentCount;

    /** 申请人联系电话 */
    private String applicantPhone;

    /** 申请人邮箱 */
    private String applicantEmail;

    /** 开始使用时间，仅时间部分，格式：HH:mm:ss */
    private String startTime;

    /** 结束使用时间，仅时间部分，格式：HH:mm:ss */
    private String endTime;
}

