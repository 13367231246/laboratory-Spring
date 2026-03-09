package com.lab.service.impl;

import com.lab.mapper.DocumentMapper;
import com.lab.pojo.Document;
import com.lab.pojo.PageBean;
import com.lab.service.AdminService;
import com.lab.service.DocumentService;
import com.lab.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private AdminService adminService;

    @Override
    public Document getById(Integer id) {
        return documentMapper.findById(id);
    }

    
    @Override
    public PageBean<Document> list(Integer pageNo, Integer pageSize) {
        // 计算偏移量
        int offset = (pageNo - 1) * pageSize;

        // 查询当前页数据和总数
        List<Document> rows = documentMapper.findPage(offset, pageSize);
        Long total = documentMapper.countAll();

        PageBean<Document> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }

    @Override
    public PageBean<Document> listByDocType(String docType, Integer pageNo, Integer pageSize) {
        // 计算偏移量
        int offset = (pageNo - 1) * pageSize;

        // 查询当前页数据和总数（只查询已发布的文档 status=1）
        List<Document> rows = documentMapper.findByDocType(docType, offset, pageSize);
        Long total = documentMapper.countByDocType(docType);

        PageBean<Document> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }

    @Override
    public PageBean<Document> listPublished(Integer pageNo, Integer pageSize) {
        int offset = (pageNo - 1) * pageSize;
        List<Document> rows = documentMapper.findPublishedPage(offset, pageSize);
        Long total = documentMapper.countPublished();
        PageBean<Document> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }
    
    /**
     * 只允许管理员账号创建文档：
     * - 使用当前登录管理员（从 token/ThreadLocal 中获取）的 id 作为 admin_id
     */
    @Override
    public void create(Document document) {
        Integer adminId = getCurrentAdminId();
        document.setAdminId(adminId);
        // 如果未显式传 status，则默认草稿 0
        if (document.getStatus() == null) {
            document.setStatus(0);
        }
        documentMapper.add(document);
    }

    @Override
    public void update(Document document) {
        // 确保只有管理员可以更新
        getCurrentAdminId();
        documentMapper.update(document);
    }

    @Override
    public void publish(Integer id) {
        // 只有管理员可以发布
        getCurrentAdminId();
        documentMapper.updateStatus(id, 1);
    }

    @Override
    public void offline(Integer id) {
        // 只有管理员可以下线
        getCurrentAdminId();
        documentMapper.updateStatus(id, 0);
    }

    @Override
    public void delete(Integer id) {
        // 只有管理员可以删除
        getCurrentAdminId();
        documentMapper.delete(id);
    }

    /**
     * 从 ThreadLocal（token解析后的claims）中直接获取管理员ID
     * token 中存储了 id、username、role 等信息
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


