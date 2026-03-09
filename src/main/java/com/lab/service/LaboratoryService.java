package com.lab.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lab.pojo.LabEquipmentItem;
import com.lab.pojo.Laboratory;
import com.lab.pojo.LaboratorySummary;
import com.lab.pojo.PageBean;

import java.util.List;

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
     * 获取所有状态为正常的实验室（status=1，不分页，普通用户可申请）
     */
    List<Laboratory> listAvailable();

    /**
     * 根据实验室ID查询该实验室内设备列表
     */
    List<LabEquipmentItem> getEquipmentByLaboratoryId(Integer laboratoryId) throws JsonProcessingException;

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
    
    /**
     * 获取实验室汇总统计
     */
    LaboratorySummary getSummary();
}

