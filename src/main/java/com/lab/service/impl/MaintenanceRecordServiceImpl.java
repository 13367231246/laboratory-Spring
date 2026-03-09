package com.lab.service.impl;

import com.lab.mapper.EquipmentMapper;
import com.lab.mapper.LaboratoryMapper;
import com.lab.mapper.MaintenanceRecordMapper;
import com.lab.mapper.UserManagementMapper;
import com.lab.pojo.Equipment;
import com.lab.pojo.Laboratory;
import com.lab.pojo.MaintenanceRecord;
import com.lab.pojo.User;
import com.lab.service.MaintenanceRecordService;
import com.lab.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {

    private static final int STATUS_WAITING = 0;

    @Autowired
    private MaintenanceRecordMapper maintenanceRecordMapper;

    @Autowired
    private LaboratoryMapper laboratoryMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private UserManagementMapper userManagementMapper;

    @Override
    public MaintenanceRecord apply(MaintenanceRecord record) {
        Integer reporterId = getCurrentUserId();

        if (record == null) {
            throw new RuntimeException("参数不能为空");
        }

        boolean hasLab = record.getLaboratoryId() != null;
        boolean hasEq = record.getEquipmentId() != null;
        if (hasLab == hasEq) {
            throw new RuntimeException("实验室ID与设备ID必须二选一");
        }

        if (record.getIssueType() == null || record.getIssueType().trim().isEmpty()) {
            throw new RuntimeException("问题类型不能为空");
        }
        if (record.getDescription() == null || record.getDescription().trim().isEmpty()) {
            throw new RuntimeException("问题描述不能为空");
        }

        if (hasLab) {
            Laboratory lab = laboratoryMapper.findById(record.getLaboratoryId());
            if (lab == null) {
                throw new RuntimeException("实验室不存在");
            }
        }
        if (hasEq) {
            Equipment eq = equipmentMapper.findById(record.getEquipmentId());
            if (eq == null) {
                throw new RuntimeException("设备不存在");
            }
        }

        record.setReporterId(reporterId);
        record.setReporterRole(resolveReporterRole(reporterId));
        record.setStatus(STATUS_WAITING);

        String recordNo = generateRecordNo();
        record.setRecordNo(recordNo);

        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());

        maintenanceRecordMapper.add(record);
        return maintenanceRecordMapper.findById(record.getId());
    }

    private String resolveReporterRole(Integer reporterId) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        String role = claims != null ? (String) claims.get("role") : null;
        if (role != null && !role.trim().isEmpty()) {
            return normalizeRole(role);
        }

        User user = userManagementMapper.findById(reporterId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return normalizeRole(user.getRole());
    }

    private String normalizeRole(String role) {
        if (role == null) {
            return "student";
        }
        if ("admin".equalsIgnoreCase(role)) {
            return "admin";
        }
        if ("teacher".equalsIgnoreCase(role)) {
            return "teacher";
        }
        return "student";
    }

    private String generateRecordNo() {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "MNT" + today;
        String maxNo = maintenanceRecordMapper.findMaxRecordNoByPrefix(prefix);
        int seq = 1;
        if (maxNo != null && maxNo.startsWith(prefix) && maxNo.length() == prefix.length() + 4) {
            seq = Integer.parseInt(maxNo.substring(prefix.length())) + 1;
        }
        return prefix + String.format("%04d", seq);
    }

    private Integer getCurrentUserId() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = claims != null ? (Integer) claims.get("id") : null;
        if (id == null) {
            throw new RuntimeException("请先登录");
        }
        return id;
    }
}
