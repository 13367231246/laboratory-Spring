package com.hss.service;

import com.hss.pojo.Notice;
import com.hss.pojo.PageBean;

public interface NoticeService {

    /**
     * 分页获取所有公告（管理员用）
     */
    PageBean<Notice> list(Integer pageNo, Integer pageSize);

    /**
     * 用户按公告类型分页获取公告（只返回已发布的且未过期的公告）
     */
    PageBean<Notice> listByType(String type, Integer pageNo, Integer pageSize);

    /**
     * 获取公告详情（可选择是否增加浏览次数）
     */
    Notice getById(Integer id, boolean increaseView);

    /**
     * 创建公告（发布人和初始状态在 service 内部根据当前用户和业务规则设置）
     */
    void create(Notice notice);

    /**
     * 更新公告内容（标题/内容/类型/优先级等）
     */
    void update(Notice notice);

    /**
     * 发布公告（状态置为 1）
     */
    void publish(Integer id);

    /**
     * 公告下线（状态置为 2）
     */
    void offline(Integer id);

    /**
     * 删除公告
     */
    void delete(Integer id);
}

