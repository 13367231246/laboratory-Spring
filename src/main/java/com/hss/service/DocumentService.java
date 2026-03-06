package com.hss.service;

import com.hss.pojo.Document;
import com.hss.pojo.PageBean;

public interface DocumentService {

     // 分页获取所有文档
     PageBean<Document> list(Integer pageNo, Integer pageSize);

     /**
      * 用户按文档类型分页获取文档（只返回已发布的文档）
      */
     PageBean<Document> listByDocType(String docType, Integer pageNo, Integer pageSize);

    /**
     * 获取文档详情
     */
    Document getById(Integer id);

    /**
     * 创建文档（发布人和初始状态在 service 内部根据当前用户和业务规则设置）
     */
    void create(Document document);

    /**
     * 更新文档内容（标题/内容/类型/版本等）
     */
    void update(Document document);

    /**
     * 发布文档（状态置为 1）
     */
    void publish(Integer id);

    /**
     * 文档下线（状态置为 0）
     */
    void offline(Integer id);

    /**
     * 删除文档
     */
    void delete(Integer id);
}


