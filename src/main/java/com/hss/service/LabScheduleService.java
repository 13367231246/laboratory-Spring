package com.hss.service;

import com.hss.pojo.LabSchedule;

import java.util.List;

public interface LabScheduleService {

    /**
     * 不分页查询规则，可按名称/类型/状态过滤
     */
    List<LabSchedule> listAll(String ruleName, String ruleType, Integer status);

    /**
     * 根据ID查询
     */
    LabSchedule getById(Integer id);

    /**
     * 新增规则（仅管理员）
     */
    void create(LabSchedule labSchedule);

    /**
     * 更新规则（仅管理员）
     */
    void update(LabSchedule labSchedule);

    /**
     * 删除规则（仅管理员）
     */
    void delete(Integer id);
}


