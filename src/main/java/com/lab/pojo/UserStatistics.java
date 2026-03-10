package com.lab.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatistics {
    /**
     * 实验室申请次数
     */
    private Integer labApplicationCount;
    
    /**
     * 报修次数（维修申请次数）
     */
    private Integer maintenanceApplicationCount;
    
    /**
     * 维修数量（已完成的维修数量）
     */
    private Integer maintenanceCompletedCount;
    
    /**
     * 使用的实验室数量（已批准或使用中的实验室申请涉及的实验室数量）
     */
    private Integer usedLaboratoryCount;
}
