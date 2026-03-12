package com.lab.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备申请提交的请求体（前端传入）
 */
@Data
public class EquipmentApplicationRequest {

    /** 申请的设备ID */
    private Integer equipmentId;

    /** 所在实验室ID */
    private Integer laboratoryId;

    /** 申请数量，默认为1 */
    private Integer quantity;

    /** 使用目的 */
    private String purpose;

    /** 申请人联系电话 */
    private String applicantPhone;

    /** 申请人邮箱 */
    private String applicantEmail;

    /** 开始使用时间，格式：yyyy-MM-dd HH:mm:ss */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /** 结束使用时间，格式：yyyy-MM-dd HH:mm:ss */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
