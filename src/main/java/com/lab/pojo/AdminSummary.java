package com.lab.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminSummary {
    /**
     * 实验室总数
     */
    private Integer totalLaboratories;
    
    /**
     * 用户总数
     */
    private Integer totalUsers;
    
    /**
     * 设备总数
     */
    private Integer totalEquipment;
    
    /**
     * 今日预约数（今日提交的实验室申请数量）
     */
    private Integer todayApplications;
}
