package com.lab.service.impl;

import com.lab.mapper.LaboratoryMapper;
import com.lab.mapper.MaintenancePlanMapper;
import com.lab.mapper.UserManagementMapper;
import com.lab.pojo.Laboratory;
import com.lab.pojo.MaintenancePlan;
import com.lab.pojo.PageBean;
import com.lab.pojo.User;
import com.lab.service.MaintenancePlanService;
import com.lab.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MaintenancePlanServiceImpl implements MaintenancePlanService {

    @Autowired
    private MaintenancePlanMapper maintenancePlanMapper;

    @Autowired
    private LaboratoryMapper laboratoryMapper;

    @Autowired
    private UserManagementMapper userManagementMapper;

    @Override
    public PageBean<MaintenancePlan> list(Integer pageNo, Integer pageSize) {
        getCurrentAdminId();
        int offset = (pageNo - 1) * pageSize;
        List<MaintenancePlan> rows = maintenancePlanMapper.findPage(offset, pageSize);
        Long total = maintenancePlanMapper.countAll();
        return new PageBean<>(total, rows);
    }

    @Override
    public MaintenancePlan create(MaintenancePlan plan) {
        getCurrentAdminId();
        validatePlan(plan);

        if (plan.getStatus() == null) {
            plan.setStatus(1);
        }
        maintenancePlanMapper.add(plan);
        return maintenancePlanMapper.findById(plan.getId());
    }

    @Override
    public void update(MaintenancePlan plan) {
        getCurrentAdminId();
        if (plan == null || plan.getId() == null) {
            throw new RuntimeException("计划ID不能为空");
        }
        MaintenancePlan existing = maintenancePlanMapper.findById(plan.getId());
        if (existing == null) {
            throw new RuntimeException("维护计划不存在");
        }
        validatePlan(plan);
        if (plan.getStatus() == null) {
            plan.setStatus(existing.getStatus());
        }
        maintenancePlanMapper.update(plan);
    }

    @Override
    public void delete(Integer id) {
        getCurrentAdminId();
        MaintenancePlan existing = maintenancePlanMapper.findById(id);
        if (existing == null) {
            throw new RuntimeException("维护计划不存在");
        }
        maintenancePlanMapper.delete(id);
    }

    @Override
    public void updateStatus(Integer id, Integer status) {
        getCurrentAdminId();
        MaintenancePlan existing = maintenancePlanMapper.findById(id);
        if (existing == null) {
            throw new RuntimeException("维护计划不存在");
        }
        if (status == null || (status != 0 && status != 1)) {
            throw new RuntimeException("状态参数不正确");
        }
        maintenancePlanMapper.updateStatus(id, status);
    }

    @Override
    public List<MaintenancePlan> listEnabledByLaboratoryId(Integer laboratoryId) {
        if (laboratoryId == null) {
            throw new RuntimeException("实验室ID不能为空");
        }
        Laboratory lab = laboratoryMapper.findById(laboratoryId);
        if (lab == null) {
            throw new RuntimeException("实验室不存在");
        }
        return maintenancePlanMapper.findEnabledByLaboratoryId(laboratoryId);
    }

    private void validatePlan(MaintenancePlan plan) {
        if (plan == null) {
            throw new RuntimeException("参数不能为空");
        }
        if (plan.getLaboratoryId() == null) {
            throw new RuntimeException("实验室ID不能为空");
        }
        Laboratory lab = laboratoryMapper.findById(plan.getLaboratoryId());
        if (lab == null) {
            throw new RuntimeException("实验室不存在");
        }
        if (plan.getPlanName() == null || plan.getPlanName().trim().isEmpty()) {
            throw new RuntimeException("计划名称不能为空");
        }
        if (plan.getMaintenanceType() == null || plan.getMaintenanceType().trim().isEmpty()) {
            throw new RuntimeException("维护周期类型不能为空");
        }
        if (plan.getMaintenanceContent() == null || plan.getMaintenanceContent().trim().isEmpty()) {
            throw new RuntimeException("维护内容不能为空");
        }
        if (plan.getResponsibleTeacherId() == null) {
            throw new RuntimeException("负责教师ID不能为空");
        }
        User teacher = userManagementMapper.findById(plan.getResponsibleTeacherId());
        if (teacher == null) {
            throw new RuntimeException("负责教师不存在");
        }
        if (teacher.getRole() == null || !"teacher".equalsIgnoreCase(teacher.getRole())) {
            throw new RuntimeException("负责教师必须是教师角色");
        }
        if (plan.getNextMaintenanceTime() == null) {
            throw new RuntimeException("下次维护时间不能为空");
        }
    }

    private Integer getCurrentAdminId() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = claims != null ? (Integer) claims.get("id") : null;
        String role = claims != null ? (String) claims.get("role") : null;
        if (id == null || role == null) {
            throw new RuntimeException("无权限操作，仅管理员可执行该操作");
        }
        if (!"admin".equalsIgnoreCase(role)) {
            throw new RuntimeException("无权限操作，仅管理员可执行该操作");
        }
        return id;
    }
}
