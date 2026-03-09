package com.lab.pojo;

import lombok.Data;

/**
 * 维修申请提交的请求体（前端传入）
 */
@Data
public class MaintenanceApplyRequest {

    /** 实验室ID（二选一） */
    private Integer laboratoryId;

    /** 设备ID（二选一） */
    private Integer equipmentId;

    /** 问题类型：clean / repair / accident / other */
    private String issueType;

    /** 问题描述 */
    private String description;

    /** 问题照片（多个URL用逗号分隔） */
    private String photos;
}
