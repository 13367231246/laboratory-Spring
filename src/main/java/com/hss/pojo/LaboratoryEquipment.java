package com.hss.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 对应数据库表 laboratory_equipment（实验室设备关联表）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaboratoryEquipment {

    /**
     * 关联ID
     */
    private Integer id;

    /**
     * 实验室ID
     */
    private Integer laboratoryId;

    /**
     * 设备ID
     */
    private Integer equipmentId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

