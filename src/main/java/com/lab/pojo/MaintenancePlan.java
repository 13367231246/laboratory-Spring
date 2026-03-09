package com.lab.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 定期维护计划实体类
 * 对应数据库表：maintenance_plan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenancePlan {

    private Integer id;

    private Integer laboratoryId;

    private String planName;

    /** daily / weekly / monthly / quarterly / yearly */
    private String maintenanceType;

    private String maintenanceContent;

    private Integer responsibleTeacherId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastMaintenanceTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nextMaintenanceTime;

    /** 0-停用，1-启用 */
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
