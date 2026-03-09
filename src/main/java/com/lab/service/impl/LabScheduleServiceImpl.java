package com.lab.service.impl;

import com.lab.mapper.LabScheduleMapper;
import com.lab.pojo.LabSchedule;
import com.lab.service.LabScheduleService;
import com.lab.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class LabScheduleServiceImpl implements LabScheduleService {

    @Autowired
    private LabScheduleMapper labScheduleMapper;

    @Override
    public List<LabSchedule> listAll(String ruleName, String ruleType, Integer status) {
        return labScheduleMapper.findAll(ruleName, ruleType, status);
    }

    @Override
    public LabSchedule getById(Integer id) {
        return labScheduleMapper.findById(id);
    }

    @Override
    public List<LabSchedule> listByLaboratoryId(Integer laboratoryId) {
        return labScheduleMapper.findByLaboratoryIdAndStatus(laboratoryId);
    }

    @Override
    @Transactional
    public void create(LabSchedule labSchedule) {
        // 校验管理员权限
        getCurrentAdminId();

        validate(labSchedule, false);
        if (labSchedule.getStatus() == null) {
            labSchedule.setStatus(1);
        }

        labScheduleMapper.add(labSchedule);
    }

    @Override
    @Transactional
    public void update(LabSchedule labSchedule) {
        // 校验管理员权限
        getCurrentAdminId();

        validate(labSchedule, true);
        LabSchedule existing = labScheduleMapper.findById(labSchedule.getId());
        if (existing == null) {
            throw new RuntimeException("规则不存在");
        }

        if (labSchedule.getStatus() == null) {
            labSchedule.setStatus(existing.getStatus() == null ? 1 : existing.getStatus());
        }

        labScheduleMapper.update(labSchedule);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        // 校验管理员权限
        getCurrentAdminId();

        if (id == null) {
            throw new RuntimeException("参数不能为空");
        }
        labScheduleMapper.delete(id);
    }

    private void validate(LabSchedule labSchedule, boolean requireId) {
        if (labSchedule == null) {
            throw new RuntimeException("参数不能为空");
        }
        if (requireId && labSchedule.getId() == null) {
            throw new RuntimeException("参数不能为空");
        }
        if (labSchedule.getRuleName() == null || labSchedule.getRuleName().trim().isEmpty()) {
            throw new RuntimeException("规则名称不能为空");
        }
        if (labSchedule.getRuleType() == null || labSchedule.getRuleType().trim().isEmpty()) {
            throw new RuntimeException("规则类型不能为空");
        }
        if (labSchedule.getLaboratoryIds() == null || labSchedule.getLaboratoryIds().trim().isEmpty()) {
            throw new RuntimeException("适用实验室不能为空");
        }
        if (labSchedule.getTimeSlots() == null || labSchedule.getTimeSlots().trim().isEmpty()) {
            throw new RuntimeException("时间段不能为空");
        }
        LocalDate start = labSchedule.getStartDate();
        LocalDate end = labSchedule.getEndDate();
        if (start == null || end == null) {
            throw new RuntimeException("开始日期和结束日期不能为空");
        }
        if (start.isAfter(end)) {
            throw new RuntimeException("开始日期不能晚于结束日期");
        }
        if (labSchedule.getStatus() != null && labSchedule.getStatus() != 0 && labSchedule.getStatus() != 1) {
            throw new RuntimeException("状态值不合法");
        }
    }

    /**
     * 从 ThreadLocal（token解析后的claims）中直接获取管理员ID
     */
    private Integer getCurrentAdminId() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer adminId = (Integer) claims.get("id");
        if (adminId == null) {
            throw new RuntimeException("无权限操作，仅管理员可执行该操作");
        }
        return adminId;
    }
}


