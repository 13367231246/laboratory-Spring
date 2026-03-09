package com.lab.service;

import com.lab.pojo.LabApplication;
import com.lab.pojo.PageBean;

public interface LabApplicationService {

    /**
     * 普通用户提交实验室申请
     */
    LabApplication apply(LabApplication application);

    /**
     * 普通用户撤销自己的申请（仅待审核、已批准可撤销）
     */
    void cancel(Integer id);

    /**
     * 普通用户完成申请（仅已批准或使用中的申请可完成）
     */
    void finish(Integer id);

    /**
     * 普通用户删除申请（仅已完成的申请可删除）
     */
    void delete(Integer id);

    /**
     * 管理员：审核通过申请（将状态置为已批准，记录审核人和审核意见）
     */
    void approve(Integer id, String reviewComment);

    /**
     * 管理员：审核拒绝申请（将状态置为已拒绝，记录审核人和审核意见）
     */
    void reject(Integer id, String reviewComment);

    /**
     * 管理员：删除任意申请记录
     */
    void adminDelete(Integer id);

    /**
     * 管理员：分页查询所有申请记录
     */
    PageBean<LabApplication> listAll(Integer pageNo, Integer pageSize);

    /**
     * 分页查询当前用户的申请记录
     */
    PageBean<LabApplication> listMyApplications(Integer pageNo, Integer pageSize);

    /**
     * 获取申请详情（仅本人或管理员可看）
     */
    LabApplication getDetail(Integer id);

    /**
     * 定时任务：将“使用中”且超过结束时间+3小时的申请自动置为“已完成”
     */
    void autoCompleteOverdue();
}
