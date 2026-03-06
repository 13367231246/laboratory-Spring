package com.hss.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * 对应数据库表 time_slot（时间段定义表）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
    /**
     * 时间段ID
     */
    private Integer id;

    /**
     * 时间段名称（如：上午第一节）
     */
    private String slotName;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}


