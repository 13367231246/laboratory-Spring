package com.lab.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 对应数据库表 laboratory（实验室表）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Laboratory {

    /**
     * 实验室ID
     */
    private Integer id;

    /**
     * 实验室房间号
     */
    private String labNumber;

    /**
     * 实验室名称
     */
    private String labName;

    /**
     * 所属学院（JSON格式，可包含多个学院）
     * 示例：[{"id": 1, "name": "信息科学与技术学院"}]
     */
    private String colleges;

    /**
     * 责任教师ID
     */
    private Integer responsibleTeacherId;

    /**
     * 具体位置
     */
    private String location;

    /**
     * 容纳人数
     */
    private Integer capacity;

    /**
     * 面积
     */
    private BigDecimal area;

    /**
     * 设备总数
     */
    private Integer equipmentCount;

    /**
     * 设备列表（JSON格式）
     * 示例：
     * [
     *   {"id":1,"name":"台式电脑","asset_number":"ZC001","count":30},
     *   {"id":2,"name":"投影仪","asset_number":"JY001","count":2}
     * ]
     */
    private String equipmentList;

    /**
     * 状态：0-禁用，1-正常
     */
    private Integer status;

    /**
     * 实验室描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

