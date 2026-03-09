package com.lab.service.impl;

import com.lab.mapper.EquipmentMapper;
import com.lab.mapper.LaboratoryMapper;
import com.lab.mapper.MaintenanceRecordMapper;
import com.lab.mapper.UserManagementMapper;
import com.lab.pojo.Equipment;
import com.lab.pojo.Laboratory;
import com.lab.pojo.MaintenanceRecord;
import com.lab.pojo.MaintenanceSummary;
import com.lab.pojo.PageBean;
import com.lab.pojo.User;
import com.lab.service.MaintenanceRecordService;
import com.lab.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.time.LocalDate;

@Service
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {

    private static final int STATUS_WAITING = 0;
    private static final int STATUS_REPAIRING = 1;
    private static final int STATUS_COMPLETED = 2;
    private static final int STATUS_NO_NEED = 3;

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
        if (!hasLab && !hasEq) {
            throw new RuntimeException("实验室ID与设备ID至少填写一个");
        }

        if (record.getIssueType() == null || record.getIssueType().trim().isEmpty()) {
            throw new RuntimeException("问题类型不能为空");
        }
        if (record.getDescription() == null || record.getDescription().trim().isEmpty()) {
            throw new RuntimeException("问题描述不能为空");
        }

        Laboratory lab = null;
        if (hasLab) {
            lab = laboratoryMapper.findById(record.getLaboratoryId());
            if (lab == null) {
                throw new RuntimeException("实验室不存在");
            }
        }
        Equipment eq = null;
        if (hasEq) {
            eq = equipmentMapper.findById(record.getEquipmentId());
            if (eq == null) {
                throw new RuntimeException("设备不存在");
            }
        }

        // 如果同时传了实验室ID与设备ID，做一致性校验：设备必须属于该实验室
        if (hasLab && hasEq) {
            String equipmentList = lab != null ? lab.getEquipmentList() : null;
            if (equipmentList != null && !equipmentList.trim().isEmpty() && !"[]".equals(equipmentList.trim())) {
                Pattern p = Pattern.compile("\\\"id\\\"\\s*:\\s*" + record.getEquipmentId());
                if (!p.matcher(equipmentList).find()) {
                    throw new RuntimeException("该设备不属于该实验室，请检查实验室ID与设备ID是否匹配");
                }
            }
        }

        if (lab != null) {
            record.setLabNumber(lab.getLabNumber());
            record.setLabName(lab.getLabName());
            record.setLocation(lab.getLocation());
        }
        if (eq != null) {
            record.setEquipmentName(eq.getEquipmentName());
            record.setEquipmentType(eq.getEquipmentType());
            record.setEquipmentModel(eq.getModel());
            record.setAssetNumber(eq.getAssetNumber());
        }

        record.setReporterId(reporterId);
        record.setReporterRole(resolveReporterRole(reporterId));

        Map<String, Object> claims = ThreadLocalUtil.get();
        String reporterName = claims != null ? (String) claims.get("username") : null;
        if (reporterName == null || reporterName.trim().isEmpty()) {
            User reporter = userManagementMapper.findById(reporterId);
            if (reporter != null) {
                reporterName = reporter.getRealName();
            }
        }
        record.setReporterName(reporterName);

        record.setStatus(STATUS_WAITING);

        String recordNo = generateRecordNo();
        record.setRecordNo(recordNo);

        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());

        maintenanceRecordMapper.add(record);
        return maintenanceRecordMapper.findById(record.getId());
    }

    @Override
    public PageBean<MaintenanceRecord> listMy(Integer pageNo, Integer pageSize) {
        Integer userId = getCurrentUserId();
        int offset = (pageNo - 1) * pageSize;
        List<MaintenanceRecord> rows = maintenanceRecordMapper.findByReporterId(userId, offset, pageSize);
        Long total = maintenanceRecordMapper.countByReporterId(userId);
        return new PageBean<>(total, rows);
    }

    @Override
    public PageBean<MaintenanceRecord> listTodoAsTeacher(Integer pageNo, Integer pageSize) {
        Integer teacherId = getCurrentTeacherId();
        int offset = (pageNo - 1) * pageSize;
        List<MaintenanceRecord> rows = maintenanceRecordMapper.findTodoByTeacherId(teacherId, offset, pageSize);
        Long total = maintenanceRecordMapper.countTodoByTeacherId(teacherId);
        return new PageBean<>(total, rows);
    }

    @Override
    public PageBean<MaintenanceRecord> listAll(Integer pageNo, Integer pageSize) {
        getCurrentAdminId();
        int offset = (pageNo - 1) * pageSize;
        List<MaintenanceRecord> rows = maintenanceRecordMapper.findAll(offset, pageSize);
        Long total = maintenanceRecordMapper.countAll();
        return new PageBean<>(total, rows);
    }

    @Override
    public void assignTeacher(Integer recordId, Integer teacherId) {
        getCurrentAdminId();
        MaintenanceRecord record = maintenanceRecordMapper.findById(recordId);
        if (record == null) {
            throw new RuntimeException("维修记录不存在");
        }
        if (record.getStatus() != null && record.getStatus() == STATUS_COMPLETED) {
            throw new RuntimeException("该维修已完成，无法指派");
        }
        User teacher = userManagementMapper.findById(teacherId);
        if (teacher == null) {
            throw new RuntimeException("教师不存在");
        }
        if (teacher.getRole() == null || !"teacher".equalsIgnoreCase(teacher.getRole())) {
            throw new RuntimeException("指派对象必须是教师");
        }
        String teacherName = teacher.getRealName();
        maintenanceRecordMapper.assignTeacher(recordId, teacherId, teacherName);
    }

    @Override
    public void startRepair(Integer recordId) {
        Integer teacherId = getCurrentTeacherId();
        MaintenanceRecord record = maintenanceRecordMapper.findById(recordId);
        if (record == null) {
            throw new RuntimeException("维修记录不存在");
        }
        if (record.getAssignedTeacherId() == null || !record.getAssignedTeacherId().equals(teacherId)) {
            throw new RuntimeException("无权限操作，仅指派的教师可开始维修");
        }
        if (record.getStatus() == null || record.getStatus() != STATUS_WAITING) {
            throw new RuntimeException("当前状态不可开始维修");
        }
        maintenanceRecordMapper.startRepair(recordId);
    }

    @Override
    public void completeRepair(Integer recordId, String repairResult, java.math.BigDecimal cost) {
        Integer teacherId = getCurrentTeacherId();
        MaintenanceRecord record = maintenanceRecordMapper.findById(recordId);
        if (record == null) {
            throw new RuntimeException("维修记录不存在");
        }
        if (record.getAssignedTeacherId() == null || !record.getAssignedTeacherId().equals(teacherId)) {
            throw new RuntimeException("无权限操作，仅指派的教师可完成维修");
        }
        if (record.getStatus() == null || record.getStatus() != STATUS_REPAIRING) {
            throw new RuntimeException("当前状态不可完成维修");
        }
        if (repairResult == null || repairResult.trim().isEmpty()) {
            throw new RuntimeException("维修结果不能为空");
        }
        maintenanceRecordMapper.completeRepair(recordId, repairResult, cost);
    }

    @Override
    public MaintenanceSummary summaryAll() {
        Long waiting = maintenanceRecordMapper.countByStatus(STATUS_WAITING);
        Long repairing = maintenanceRecordMapper.countByStatus(STATUS_REPAIRING);
        Long completed = maintenanceRecordMapper.countByStatus(STATUS_COMPLETED);
        Long noNeed = maintenanceRecordMapper.countByStatus(STATUS_NO_NEED);
        return new MaintenanceSummary(waiting, repairing, completed, noNeed);
    }

    @Override
    public MaintenanceSummary summaryForTeacher() {
        Integer teacherId = getCurrentTeacherId();
        Long waiting = maintenanceRecordMapper.countByTeacherAndStatus(teacherId, STATUS_WAITING);
        Long repairing = maintenanceRecordMapper.countByTeacherAndStatus(teacherId, STATUS_REPAIRING);
        Long completed = maintenanceRecordMapper.countByTeacherAndStatus(teacherId, STATUS_COMPLETED);
        Long noNeed = maintenanceRecordMapper.countByTeacherAndStatus(teacherId, STATUS_NO_NEED);
        return new MaintenanceSummary(waiting, repairing, completed, noNeed);
    }

    @Override
    public void markNoNeed(Integer recordId) {
        Integer teacherId = getCurrentTeacherId();
        MaintenanceRecord record = maintenanceRecordMapper.findById(recordId);
        if (record == null) {
            throw new RuntimeException("维修记录不存在");
        }
        if (record.getAssignedTeacherId() == null || !record.getAssignedTeacherId().equals(teacherId)) {
            throw new RuntimeException("无权限操作，仅指派的教师可执行该操作");
        }
        if (record.getStatus() == null) {
            throw new RuntimeException("当前状态不可操作");
        }
        if (record.getStatus() == STATUS_COMPLETED) {
            throw new RuntimeException("已完成维修的记录不可标记为不需要维护");
        }
        if (record.getStatus() == STATUS_NO_NEED) {
            return;
        }
        maintenanceRecordMapper.updateStatus(recordId, STATUS_NO_NEED);
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

    @Override
    public List<MaintenanceRecord> listTodayApplications(Integer limit) {
        LocalDate today = LocalDate.now();
        return maintenanceRecordMapper.findByDate(today, limit);
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

    private Integer getCurrentTeacherId() {
        Integer id = getCurrentUserId();
        User user = userManagementMapper.findById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getRole() == null || !"teacher".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("无权限操作，仅教师可执行该操作");
        }
        return id;
    }
}
