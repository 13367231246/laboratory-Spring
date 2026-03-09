package com.lab.service;

import com.lab.pojo.Equipment;
import com.lab.pojo.PageBean;

import java.util.List;

public interface EquipmentService {

    /**
     * 分页获取所有设备
     */
    PageBean<Equipment> list(Integer pageNo, Integer pageSize);

    /**
     * 不分页查询设备列表，可按设备名模糊搜索
     *
     * @param equipmentName 设备名（可为空，为空时查询全部）
     * @return 设备列表
     */
    List<Equipment> listAll(String equipmentName);

    /**
     * 根据ID获取设备详情
     */
    Equipment getById(Integer id);

    /**
     * 新增设备
     */
    void create(Equipment equipment);

    /**
     * 更新设备信息
     */
    void update(Equipment equipment);

    /**
     * 删除设备
     */
    void delete(Integer id);
}

