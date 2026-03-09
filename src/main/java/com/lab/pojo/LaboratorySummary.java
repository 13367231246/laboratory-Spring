package com.lab.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaboratorySummary {
    /**
     * 可用实验室数量（status=1）
     */
    private Integer availableLabs;
    
    /**
     * 使用中实验室数量（status=2）
     */
    private Integer inUseLabs;
    
    /**
     * 待维修设备数量（status=0 或 1 的维修记录）
     */
    private Integer pendingMaintenanceEquipment;
    
    /**
     * 今日申请数量（今日提交的维修申请）
     */
    private Integer todayApplications;
}
