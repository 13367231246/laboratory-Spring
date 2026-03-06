package com.hss.service.impl;

import com.hss.mapper.TimeSlotMapper;
import com.hss.pojo.TimeSlot;
import com.hss.service.TimeSlotService;
import com.hss.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

    @Autowired
    private TimeSlotMapper timeSlotMapper;

    @Override
    public List<TimeSlot> listAll(String slotName, Integer status) {
        return timeSlotMapper.findAll(slotName, status);
    }

    @Override
    public TimeSlot getById(Integer id) {
        return timeSlotMapper.findById(id);
    }

    @Override
    public void create(TimeSlot timeSlot) {
        // 校验管理员权限
        getCurrentAdminId();

        if (timeSlot == null) {
            throw new RuntimeException("参数不能为空");
        }
        if (timeSlot.getSlotName() == null || timeSlot.getSlotName().trim().isEmpty()) {
            throw new RuntimeException("时间段名称不能为空");
        }
        if (timeSlot.getStartTime() == null || timeSlot.getEndTime() == null) {
            throw new RuntimeException("开始时间和结束时间不能为空");
        }
        if (!timeSlot.getStartTime().isBefore(timeSlot.getEndTime())) {
            throw new RuntimeException("开始时间必须早于结束时间");
        }
        if (timeSlot.getSortOrder() == null) {
            timeSlot.setSortOrder(0);
        }
        if (timeSlot.getStatus() == null) {
            timeSlot.setStatus(1);
        }

        timeSlotMapper.add(timeSlot);
    }

    @Override
    public void update(TimeSlot timeSlot) {
        // 校验管理员权限
        getCurrentAdminId();

        if (timeSlot == null || timeSlot.getId() == null) {
            throw new RuntimeException("参数不能为空");
        }
        TimeSlot existing = timeSlotMapper.findById(timeSlot.getId());
        if (existing == null) {
            throw new RuntimeException("时间段不存在");
        }

        if (timeSlot.getSlotName() == null || timeSlot.getSlotName().trim().isEmpty()) {
            throw new RuntimeException("时间段名称不能为空");
        }
        if (timeSlot.getStartTime() == null || timeSlot.getEndTime() == null) {
            throw new RuntimeException("开始时间和结束时间不能为空");
        }
        if (!timeSlot.getStartTime().isBefore(timeSlot.getEndTime())) {
            throw new RuntimeException("开始时间必须早于结束时间");
        }
        if (timeSlot.getSortOrder() == null) {
            timeSlot.setSortOrder(existing.getSortOrder() == null ? 0 : existing.getSortOrder());
        }
        if (timeSlot.getStatus() == null) {
            timeSlot.setStatus(existing.getStatus() == null ? 1 : existing.getStatus());
        }

        timeSlotMapper.update(timeSlot);
    }

    @Override
    public void delete(Integer id) {
        // 校验管理员权限
        getCurrentAdminId();

        if (id == null) {
            throw new RuntimeException("参数不能为空");
        }
        timeSlotMapper.delete(id);
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


