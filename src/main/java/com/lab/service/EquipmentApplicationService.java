package com.lab.service;

import com.lab.pojo.EquipmentApplication;
import com.lab.pojo.PageBean;

public interface EquipmentApplicationService {

    /** 用户提交设备申请 */
    EquipmentApplication apply(EquipmentApplication application);

    /** 用户撤销自己的申请（仅待审核、已批准可撤销） */
    void cancel(Integer id);

    /** 用户完成使用（仅已批准或使用中可完成） */
    void finish(Integer id);

    /** 用户删除申请记录（仅已完成或已取消可删除） */
    void delete(Integer id);

    /** 管理员：审核通过 */
    void approve(Integer id, String reviewComment);

    /** 管理员：审核拒绝（必须填写审核意见） */
    void reject(Integer id, String reviewComment);

    /** 管理员：删除任意申请记录 */
    void adminDelete(Integer id);

    /** 管理员：分页查询所有申请记录 */
    PageBean<EquipmentApplication> listAll(Integer pageNo, Integer pageSize);

    /** 用户：分页查询我的申请记录 */
    PageBean<EquipmentApplication> listMyApplications(Integer pageNo, Integer pageSize);

    /** 获取申请详情 */
    EquipmentApplication getDetail(Integer id);

    /** 定时任务：自动完成超时未结束的申请 */
    void autoCompleteOverdue();
}
