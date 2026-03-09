package com.lab.service;

import com.lab.pojo.TimeSlot;

import java.util.List;

public interface TimeSlotService {

    /**
     * 不分页查询时间段，可按名称/状态过滤
     */
    List<TimeSlot> listAll(String slotName, Integer status);

    /**
     * 根据ID查询
     */
    TimeSlot getById(Integer id);

    /**
     * 新增时间段
     */
    void create(TimeSlot timeSlot);

    /**
     * 更新时间段
     */
    void update(TimeSlot timeSlot);

    /**
     * 删除时间段
     */
    void delete(Integer id);
}


