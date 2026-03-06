package com.hss.service;

import com.hss.pojo.Laboratory;
import com.hss.pojo.PageBean;

public interface LaboratoryService {

    /**
     * 分页获取所有实验室
     */
    PageBean<Laboratory> list(Integer pageNo, Integer pageSize);

    /**
     * 根据ID获取实验室详情
     */
    Laboratory getById(Integer id);

    /**
     * 新增实验室
     */
    void create(Laboratory laboratory);

    /**
     * 更新实验室信息
     */
    void update(Laboratory laboratory);

    /**
     * 删除实验室
     */
    void delete(Integer id);

    /**
     * 添加设备到实验室
     */
    void addEquipment(Integer laboratoryId, Integer equipmentId, Integer quantity);
}

