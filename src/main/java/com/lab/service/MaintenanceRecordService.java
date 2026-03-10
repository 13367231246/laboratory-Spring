package com.lab.service;

import com.lab.pojo.MaintenanceRecord;
import com.lab.pojo.MaintenanceSummary;
import com.lab.pojo.PageBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MaintenanceRecordService {

    /**
     * 用户：提交维修申请
     */
    MaintenanceRecord apply(MaintenanceRecord record);

    /** 用户：分页查询我的维修记录 */
    PageBean<MaintenanceRecord> listMy(Integer pageNo, Integer pageSize);
    
    /** 用户：获取我的报修任务详情 */
    MaintenanceRecord getMyMaintenanceDetail(Integer recordId);
    
    /** 用户：删除我的报修任务（仅待审核状态可删除） */
    void deleteMyMaintenance(Integer recordId);
    
    /** 用户：获取我的报修进度统计 */
    Map<String, Integer> getMyMaintenanceProgress();

    /** 老师：分页查询待处理维修记录（已指派给我，状态0/1） */
    PageBean<MaintenanceRecord> listTodoAsTeacher(Integer pageNo, Integer pageSize);

    /** 管理员：分页查询全部维修记录 */
    PageBean<MaintenanceRecord> listAll(Integer pageNo, Integer pageSize);

    /** 管理员：指派维修教师 */
    void assignTeacher(Integer recordId, Integer teacherId);
    
    /** 管理员：删除维修任务 */
    void deleteMaintenanceRecord(Integer recordId);

    /** 教师：开始维修（状态置为1，记录开始时间） */
    void startRepair(Integer recordId);

    /** 教师：完成维修（状态置为2，记录结果/费用/完成时间） */
    void completeRepair(Integer recordId, String repairResult, BigDecimal cost);

    /** 所有用户：全局汇总（待维护/进行中/已完成/不需要维护） */
    MaintenanceSummary summaryAll();

    /** 教师：我的汇总（仅统计指派给我的记录） */
    MaintenanceSummary summaryForTeacher();

    /** 教师：标记不需要维护（状态置为3） */
    void markNoNeed(Integer recordId);
    
    /** 所有用户：查看今日申请信息（限制条数） */
    List<MaintenanceRecord> listTodayApplications(Integer limit);
}
