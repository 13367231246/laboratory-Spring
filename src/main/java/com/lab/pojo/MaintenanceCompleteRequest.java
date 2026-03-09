package com.lab.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 教师完成维修的请求体
 */
@Data
public class MaintenanceCompleteRequest {

    private String repairResult;

    private BigDecimal cost;
}
