package com.lab.service.impl;

import com.lab.mapper.EquipmentApplicationMapper;
import com.lab.mapper.EquipmentMapper;
import com.lab.mapper.LaboratoryMapper;
import com.lab.mapper.UserManagementMapper;
import com.lab.pojo.Equipment;
import com.lab.pojo.EquipmentApplication;
import com.lab.pojo.Laboratory;
import com.lab.pojo.PageBean;
import com.lab.pojo.User;
import com.lab.service.EquipmentApplicationService;
import com.lab.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class EquipmentApplicationServiceImpl implements EquipmentApplicationService {

    private static final int STATUS_PENDING = 0;
    private static final int STATUS_APPROVED = 1;
    private static final int STATUS_REJECTED = 2;
    private static final int STATUS_IN_USE = 3;
    private static final int STATUS_COMPLETED = 4;
    private static final int STATUS_CANCELLED = 5;

    @Autowired
    private EquipmentApplicationMapper equipmentApplicationMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private LaboratoryMapper laboratoryMapper;

    @Autowired
    private UserManagementMapper userManagementMapper;

    @Override
    public EquipmentApplication apply(EquipmentApplication application) {
        Integer userId = getCurrentUserId();
        application.setApplicantId(userId);

        User user = userManagementMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        String role = user.getRole();
        application.setApplicantRole("teacher".equalsIgnoreCase(role) ? "teacher" : "student");
        application.setStatus(STATUS_PENDING);

        if (application.getQuantity() == null || application.getQuantity() <= 0) {
            application.setQuantity(1);
        }

        Equipment equipment = equipmentMapper.findById(application.getEquipmentId());
        if (equipment == null) {
            throw new RuntimeException("设备不存在");
        }

        application.setEquipmentName(equipment.getEquipmentName());
        application.setEquipmentType(equipment.getEquipmentType());
        application.setEquipmentModel(equipment.getModel());
        application.setAssetNumber(equipment.getAssetNumber());

        Laboratory laboratory = laboratoryMapper.findById(application.getLaboratoryId());
        if (laboratory == null) {
            throw new RuntimeException("实验室不存在");
        }
        application.setLabNumber(laboratory.getLabNumber());
        application.setLabName(laboratory.getLabName());
        application.setLocation(laboratory.getLocation());

        if (equipment.getAvailableQuantity() == null || equipment.getAvailableQuantity() <= 0) {
            throw new RuntimeException("该设备当前无可用库存");
        }
        if (application.getQuantity() > equipment.getAvailableQuantity()) {
            throw new RuntimeException("申请数量超过设备可用数量，当前可用：" + equipment.getAvailableQuantity());
        }

        if (application.getEndTime() == null || application.getStartTime() == null) {
            throw new RuntimeException("使用时间不能为空");
        }
        if (!application.getEndTime().isAfter(application.getStartTime())) {
            throw new RuntimeException("结束时间必须晚于开始时间");
        }

        Integer occupied = equipmentApplicationMapper.sumOccupiedQuantity(
                application.getEquipmentId(), application.getStartTime(), application.getEndTime());
        if (occupied == null) occupied = 0;
        if (occupied + application.getQuantity() > equipment.getAvailableQuantity()) {
            throw new RuntimeException("该时间段该设备可用数量不足，该时段已被预约数量：" + occupied);
        }

        String applicationNo = generateApplicationNo();
        application.setApplicationNo(applicationNo);
        equipmentApplicationMapper.add(application);
        return equipmentApplicationMapper.findById(application.getId());
    }

    @Override
    public void cancel(Integer id) {
        Integer userId = getCurrentUserId();
        EquipmentApplication app = equipmentApplicationMapper.findById(id);
        if (app == null) {
            throw new RuntimeException("申请记录不存在");
        }
        if (!app.getApplicantId().equals(userId)) {
            throw new RuntimeException("只能撤销本人的申请");
        }
        if (app.getStatus() != STATUS_PENDING && app.getStatus() != STATUS_APPROVED) {
            throw new RuntimeException("当前状态不可撤销，仅待审核或已批准的申请可撤销");
        }
        equipmentApplicationMapper.updateStatus(id, STATUS_CANCELLED);
    }

    @Override
    public void finish(Integer id) {
        Integer userId = getCurrentUserId();
        EquipmentApplication app = equipmentApplicationMapper.findById(id);
        if (app == null) {
            throw new RuntimeException("申请记录不存在");
        }
        if (!app.getApplicantId().equals(userId)) {
            throw new RuntimeException("只能完成本人的申请");
        }
        if (app.getStatus() != STATUS_APPROVED && app.getStatus() != STATUS_IN_USE) {
            throw new RuntimeException("仅已批准或使用中的申请可以完成");
        }
        equipmentApplicationMapper.complete(id, STATUS_COMPLETED);
    }

    @Override
    public void delete(Integer id) {
        Integer userId = getCurrentUserId();
        EquipmentApplication app = equipmentApplicationMapper.findById(id);
        if (app == null) {
            throw new RuntimeException("申请记录不存在");
        }
        if (!app.getApplicantId().equals(userId)) {
            throw new RuntimeException("只能删除本人的申请");
        }
        if (app.getStatus() != STATUS_COMPLETED && app.getStatus() != STATUS_CANCELLED) {
            throw new RuntimeException("只能删除已完成或已取消的申请");
        }
        equipmentApplicationMapper.delete(id);
    }

    @Override
    public void approve(Integer id, String reviewComment) {
        Integer reviewerId = getCurrentReviewerId();
        EquipmentApplication app = equipmentApplicationMapper.findById(id);
        if (app == null) {
            throw new RuntimeException("申请记录不存在");
        }
        if (app.getStatus() != STATUS_PENDING) {
            throw new RuntimeException("仅待审核的申请可以执行审核通过");
        }
        Map<String, Object> claims = ThreadLocalUtil.get();
        String reviewerName = claims != null ? (String) claims.get("username") : null;
        String reviewerRole = claims != null ? (String) claims.get("role") : null;
        equipmentApplicationMapper.review(id, STATUS_APPROVED, reviewerId, reviewerName, reviewerRole, reviewComment);
    }

    @Override
    public void reject(Integer id, String reviewComment) {
        Integer reviewerId = getCurrentReviewerId();
        EquipmentApplication app = equipmentApplicationMapper.findById(id);
        if (app == null) {
            throw new RuntimeException("申请记录不存在");
        }
        if (app.getStatus() != STATUS_PENDING) {
            throw new RuntimeException("仅待审核的申请可以执行审核拒绝");
        }
        if (reviewComment == null || reviewComment.trim().isEmpty()) {
            throw new RuntimeException("拒绝申请时必须填写审核意见");
        }
        Map<String, Object> claims = ThreadLocalUtil.get();
        String reviewerName = claims != null ? (String) claims.get("username") : null;
        String reviewerRole = claims != null ? (String) claims.get("role") : null;
        equipmentApplicationMapper.review(id, STATUS_REJECTED, reviewerId, reviewerName, reviewerRole, reviewComment);
    }

    @Override
    public void adminDelete(Integer id) {
        getCurrentReviewerId();
        EquipmentApplication app = equipmentApplicationMapper.findById(id);
        if (app == null) {
            throw new RuntimeException("申请记录不存在");
        }
        equipmentApplicationMapper.delete(id);
    }

    @Override
    public PageBean<EquipmentApplication> listAll(Integer pageNo, Integer pageSize) {
        getCurrentReviewerId();
        int offset = (pageNo - 1) * pageSize;
        List<EquipmentApplication> rows = equipmentApplicationMapper.findAll(offset, pageSize);
        Long total = equipmentApplicationMapper.countAll();
        PageBean<EquipmentApplication> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }

    @Override
    public PageBean<EquipmentApplication> listMyApplications(Integer pageNo, Integer pageSize) {
        Integer userId = getCurrentUserId();
        int offset = (pageNo - 1) * pageSize;
        List<EquipmentApplication> rows = equipmentApplicationMapper.findByApplicantId(userId, offset, pageSize);
        Long total = equipmentApplicationMapper.countByApplicantId(userId);
        PageBean<EquipmentApplication> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }

    @Override
    public EquipmentApplication getDetail(Integer id) {
        Integer userId = getCurrentUserId();
        EquipmentApplication app = equipmentApplicationMapper.findById(id);
        if (app == null) {
            throw new RuntimeException("申请记录不存在");
        }
        if (!app.getApplicantId().equals(userId)) {
            throw new RuntimeException("无权查看该申请");
        }
        return app;
    }

    @Override
    public void autoCompleteOverdue() {
        List<EquipmentApplication> list = equipmentApplicationMapper.findUsingPastDeadline();
        for (EquipmentApplication app : list) {
            equipmentApplicationMapper.complete(app.getId(), STATUS_COMPLETED);
        }
    }

    private String generateApplicationNo() {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "EQP" + today;
        String maxNo = equipmentApplicationMapper.findMaxApplicationNoToday();
        int seq = 1;
        if (maxNo != null && maxNo.startsWith(prefix) && maxNo.length() == prefix.length() + 4) {
            seq = Integer.parseInt(maxNo.substring(prefix.length())) + 1;
        }
        return prefix + String.format("%04d", seq);
    }

    private Integer getCurrentUserId() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        if (id == null) {
            throw new RuntimeException("请先登录");
        }
        return id;
    }

    private Integer getCurrentReviewerId() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        String role = (String) claims.get("role");
        if (id == null || role == null) {
            throw new RuntimeException("无权限操作，仅管理员可执行该操作");
        }
        if (!"admin".equals(role)) {
            throw new RuntimeException("无权限操作，仅管理员可执行该操作");
        }
        return id;
    }
}
