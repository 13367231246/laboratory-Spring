package com.lab.service.impl;

import com.lab.mapper.LabApplicationMapper;
import com.lab.mapper.LaboratoryMapper;
import com.lab.mapper.UserManagementMapper;
import com.lab.pojo.LabApplication;
import com.lab.pojo.Laboratory;
import com.lab.pojo.PageBean;
import com.lab.pojo.User;
import com.lab.service.LabApplicationService;
import com.lab.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class LabApplicationServiceImpl implements LabApplicationService {

    private static final int STATUS_PENDING = 0;
    private static final int STATUS_APPROVED = 1;
    private static final int STATUS_REJECTED = 2;
    private static final int STATUS_IN_USE = 3;
    private static final int STATUS_COMPLETED = 4;
    private static final int STATUS_CANCELLED = 5;

    @Autowired
    private LabApplicationMapper labApplicationMapper;

    @Autowired
    private LaboratoryMapper laboratoryMapper;

    @Autowired
    private UserManagementMapper userManagementMapper;

    @Override
    public LabApplication apply(LabApplication application) {
        Integer userId = getCurrentUserId();
        application.setApplicantId(userId);
        User user = userManagementMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        String role = user.getRole();
        application.setApplicantRole("teacher".equalsIgnoreCase(role) ? "teacher" : "student");
        application.setStatus(STATUS_PENDING);

        Laboratory lab = laboratoryMapper.findById(application.getLaboratoryId());
        if (lab == null) {
            throw new RuntimeException("实验室不存在");
        }
        if (lab.getStatus() == null || lab.getStatus() != 1) {
            throw new RuntimeException("该实验室暂不可申请");
        }
        application.setLabNumber(lab.getLabNumber());
        application.setLabName(lab.getLabName());
        application.setLocation(lab.getLocation());
        if (application.getEndTime().isBefore(application.getStartTime()) || application.getEndTime().equals(application.getStartTime())) {
            throw new RuntimeException("结束时间必须晚于开始时间");
        }
        List<LabApplication> overlapping = labApplicationMapper.findOverlapping(
                application.getLaboratoryId(), application.getStartTime(), application.getEndTime());
        if (!overlapping.isEmpty()) {
            throw new RuntimeException("该时间段该实验室已被预约，请选择其他时间");
        }

        String applicationNo = generateApplicationNo();
        application.setApplicationNo(applicationNo);
        labApplicationMapper.add(application);
        return labApplicationMapper.findById(application.getId());
    }

    @Override
    public void cancel(Integer id) {
        Integer userId = getCurrentUserId();
        LabApplication app = labApplicationMapper.findById(id);
        if (app == null) {
            throw new RuntimeException("申请记录不存在");
        }
        if (!app.getApplicantId().equals(userId)) {
            throw new RuntimeException("只能撤销本人的申请");
        }
        if (app.getStatus() != STATUS_PENDING && app.getStatus() != STATUS_APPROVED) {
            throw new RuntimeException("当前状态不可撤销，仅待审核或已批准的申请可撤销");
        }
        labApplicationMapper.updateStatus(id, STATUS_CANCELLED);
    }

    @Override
    public void finish(Integer id) {
        Integer userId = getCurrentUserId();
        LabApplication app = labApplicationMapper.findById(id);
        if (app == null) {
            throw new RuntimeException("申请记录不存在");
        }
        if (!app.getApplicantId().equals(userId)) {
            throw new RuntimeException("只能完成本人的申请");
        }
        if (app.getStatus() != STATUS_APPROVED && app.getStatus() != STATUS_IN_USE) {
            throw new RuntimeException("仅已批准或使用中的申请可以完成");
        }
        labApplicationMapper.complete(id, STATUS_COMPLETED);
    }

    @Override
    public void delete(Integer id) {
        Integer userId = getCurrentUserId();
        LabApplication app = labApplicationMapper.findById(id);
        if (app == null) {
            throw new RuntimeException("申请记录不存在");
        }
        if (!app.getApplicantId().equals(userId)) {
            throw new RuntimeException("只能删除本人的申请");
        }
        if (app.getStatus() != STATUS_COMPLETED && app.getStatus() != STATUS_CANCELLED) {
            throw new RuntimeException("只能删除已完成或已取消的申请");
        }
        labApplicationMapper.delete(id);
    }

    @Override
    public PageBean<LabApplication> listMyApplications(Integer pageNo, Integer pageSize) {
        Integer userId = getCurrentUserId();
        int offset = (pageNo - 1) * pageSize;
        List<LabApplication> rows = labApplicationMapper.findByApplicantId(userId, offset, pageSize);
        Long total = labApplicationMapper.countByApplicantId(userId);
        PageBean<LabApplication> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }

    @Override
    public LabApplication getDetail(Integer id) {
        Integer userId = getCurrentUserId();
        LabApplication app = labApplicationMapper.findById(id);
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
        List<LabApplication> list = labApplicationMapper.findUsingPastDeadline();
        for (LabApplication app : list) {
            labApplicationMapper.complete(app.getId(), STATUS_COMPLETED);
        }
    }

    @Override
    public void approve(Integer id, String reviewComment) {
        Integer reviewerId = getCurrentReviewerId();
        LabApplication app = labApplicationMapper.findById(id);
        if (app == null) {
            throw new RuntimeException("申请记录不存在");
        }
        if (app.getStatus() != STATUS_PENDING) {
            throw new RuntimeException("仅待审核的申请可以执行审核通过");
        }
        labApplicationMapper.review(id, STATUS_APPROVED, reviewerId, reviewComment);
    }

    @Override
    public void reject(Integer id, String reviewComment) {
        Integer reviewerId = getCurrentReviewerId();
        LabApplication app = labApplicationMapper.findById(id);
        if (app == null) {
            throw new RuntimeException("申请记录不存在");
        }
        if (app.getStatus() != STATUS_PENDING) {
            throw new RuntimeException("仅待审核的申请可以执行审核拒绝");
        }
        if (reviewComment == null || reviewComment.trim().isEmpty()) {
            throw new RuntimeException("拒绝申请时必须填写审核意见");
        }
        labApplicationMapper.review(id, STATUS_REJECTED, reviewerId, reviewComment);
    }

    @Override
    public void adminDelete(Integer id) {
        Integer reviewerId = getCurrentReviewerId();
        LabApplication app = labApplicationMapper.findById(id);
        if (app == null) {
            throw new RuntimeException("申请记录不存在");
        }
        labApplicationMapper.delete(id);
    }

    @Override
    public PageBean<LabApplication> listAll(Integer pageNo, Integer pageSize) {
        getCurrentReviewerId();
        int offset = (pageNo - 1) * pageSize;
        List<LabApplication> rows = labApplicationMapper.findAll(offset, pageSize);
        Long total = labApplicationMapper.countAll();
        PageBean<LabApplication> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }

    private String generateApplicationNo() {
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "LAB" + today;
        String maxNo = labApplicationMapper.findMaxApplicationNoToday();
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

    private String getCurrentUserRole() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        String role = (String) claims.get("role");
        return role == null ? "student" : role;
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
