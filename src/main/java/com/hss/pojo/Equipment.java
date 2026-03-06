package com.hss.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 对应数据库表 equipment（设备表）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {

    /**
     * 设备ID
     */
    private Integer id;

    /**
     * 设备资产编号
     */
    private String assetNumber;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备类型
     */
    private String equipmentType;

    /**
     * 型号
     */
    private String model;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 购买日期
     */
    private LocalDate purchaseDate;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 责任教师ID
     */
    private Integer responsibleTeacherId;

    /**
     * 状态：0-正常且空闲，1-使用中，2-异常(需维修)
     */
    private Integer status;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 可用数量
     */
    private Integer availableQuantity;

    
    private Integer faultyQuantity;
    
    /**
     * 设备描述
     */
    private String description;

    /**
     * 设备图片
     */
    private String image;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}

