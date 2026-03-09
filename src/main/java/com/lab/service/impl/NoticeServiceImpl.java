package com.lab.service.impl;

import com.lab.mapper.NoticeMapper;
import com.lab.pojo.Admin;
import com.lab.pojo.Notice;
import com.lab.pojo.PageBean;
import com.lab.service.AdminService;
import com.lab.service.NoticeService;
import com.lab.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private AdminService adminService;

    @Override
    public Notice getById(Integer id, boolean increaseView) {
        if (increaseView) {
            noticeMapper.incrementViews(id);
        }
        return noticeMapper.findById(id);
    }

    @Override
    public PageBean<Notice> list(Integer pageNo, Integer pageSize) {
        // 计算偏移量
        int offset = (pageNo - 1) * pageSize;

        // 查询当前页数据和总数
        List<Notice> rows = noticeMapper.findPage(offset, pageSize);
        Long total = noticeMapper.countAll();

        PageBean<Notice> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }

    @Override
    public PageBean<Notice> listByType(String type, Integer pageNo, Integer pageSize) {
        // 计算偏移量
        int offset = (pageNo - 1) * pageSize;

        // 查询当前页数据和总数（只查询已发布的且未过期的公告）
        List<Notice> rows = noticeMapper.findByType(type, offset, pageSize);
        Long total = noticeMapper.countByType(type);

        PageBean<Notice> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }

    @Override
    public PageBean<Notice> listPublished(Integer pageNo, Integer pageSize) {
        int offset = (pageNo - 1) * pageSize;
        List<Notice> rows = noticeMapper.findPublishedPage(offset, pageSize);
        Long total = noticeMapper.countPublished();
        PageBean<Notice> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }

    /**
     * 只允许管理员账号创建公告：
     * - 使用当前登录管理员（从 token/ThreadLocal 中获取）的 id 作为 publisher_id
     * - 从 admin 表获取管理员用户名作为 publisher_name
     */
    @Override
    public void create(Notice notice) {
        Integer adminId = getCurrentAdminId();
        Admin admin = adminService.findByUsername(getCurrentUsername());
        
        notice.setPublisherId(adminId);
        notice.setPublisherName(admin.getUsername());
        notice.setViews(0);
        
        // 如果未显式传 status，则默认草稿 0
        if (notice.getStatus() == null) {
            notice.setStatus(0);
        }
        
        // 如果状态为已发布（1），设置发布时间
        if (notice.getStatus() == 1) {
            notice.setPublishTime(LocalDateTime.now());
        }
        
        noticeMapper.add(notice);
    }

    @Override
    public void update(Notice notice) {
        // 确保只有管理员可以更新
        getCurrentAdminId();
        
        // 如果状态改为已发布，设置发布时间
        if (notice.getStatus() != null && notice.getStatus() == 1) {
            Notice existing = noticeMapper.findById(notice.getId());
            if (existing != null && existing.getStatus() != 1) {
                notice.setPublishTime(LocalDateTime.now());
            }
        }
        
        noticeMapper.update(notice);
    }

    @Override
    public void publish(Integer id) {
        // 只有管理员可以发布
        getCurrentAdminId();
        Notice notice = noticeMapper.findById(id);
        if (notice != null && notice.getStatus() != 1) {
            notice.setPublishTime(LocalDateTime.now());
            noticeMapper.update(notice);
        }
        noticeMapper.updateStatus(id, 1);
    }

    @Override
    public void offline(Integer id) {
        // 只有管理员可以下线
        getCurrentAdminId();
        noticeMapper.updateStatus(id, 2);
    }

    @Override
    public void delete(Integer id) {
        // 只有管理员可以删除
        getCurrentAdminId();
        noticeMapper.delete(id);
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

    /**
     * 从 ThreadLocal 中获取当前用户名
     */
    private String getCurrentUsername() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        return (String) claims.get("username");
    }
}

