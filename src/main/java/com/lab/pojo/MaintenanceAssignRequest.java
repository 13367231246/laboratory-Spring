package com.lab.pojo;

import lombok.Data;

/**
 * 管理员指派维修教师的请求体
 */
@Data
public class MaintenanceAssignRequest {

    private Integer teacherId;
}
