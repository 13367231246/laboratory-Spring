package com.lab.service;

import com.lab.pojo.MaintenancePlan;
import com.lab.pojo.PageBean;

import java.util.List;

public interface MaintenancePlanService {

    /** 管理员：分页查询维护计划 */
    PageBean<MaintenancePlan> list(Integer pageNo, Integer pageSize);

    /** 管理员：创建维护计划 */
    MaintenancePlan create(MaintenancePlan plan);

    /** 管理员：更新维护计划 */
    void update(MaintenancePlan plan);

    /** 管理员：删除维护计划 */
    void delete(Integer id);

    /** 管理员：启用/停用维护计划 */
    void updateStatus(Integer id, Integer status);

    /** 普通用户/教师：按实验室查询启用的维护计划 */
    List<MaintenancePlan> listEnabledByLaboratoryId(Integer laboratoryId);
}
