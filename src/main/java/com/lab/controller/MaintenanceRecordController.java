package com.lab.controller;

import com.lab.pojo.MaintenanceApplyRequest;
import com.lab.pojo.MaintenanceAssignRequest;
import com.lab.pojo.MaintenanceCompleteRequest;
import com.lab.pojo.MaintenanceRecord;
import com.lab.pojo.MaintenanceSummary;
import com.lab.pojo.PageBean;
import com.lab.pojo.Result;
import com.lab.service.MaintenanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/maintenance")
@Validated
public class MaintenanceRecordController {

    @Autowired
    private MaintenanceRecordService maintenanceRecordService;

    /**
     * 用户：提交维修申请（实验室/设备二选一）
     */
    @PostMapping("/apply")
    public Result<MaintenanceRecord> apply(@RequestBody MaintenanceApplyRequest request) {
        MaintenanceRecord record = new MaintenanceRecord();
        record.setLaboratoryId(request.getLaboratoryId());
        record.setEquipmentId(request.getEquipmentId());
        record.setIssueType(request.getIssueType());
        record.setDescription(request.getDescription());
        record.setPhotos(request.getPhotos());

        MaintenanceRecord created = maintenanceRecordService.apply(record);
        return Result.success(created);
    }

    /**
     * 用户：分页查询我的维修记录
     */
    @GetMapping("/my")
    public Result<PageBean<MaintenanceRecord>> listMy(@RequestParam Integer pageNo,
                                                      @RequestParam Integer pageSize) {
        PageBean<MaintenanceRecord> pageBean = maintenanceRecordService.listMy(pageNo, pageSize);
        return Result.success(pageBean);
    }
    
    /**
     * 用户：获取我的报修任务详情
     */
    @GetMapping("/my/{id}")
    public Result<MaintenanceRecord> getMyMaintenanceDetail(@PathVariable Integer id) {
        MaintenanceRecord record = maintenanceRecordService.getMyMaintenanceDetail(id);
        return Result.success(record);
    }
    
    /**
     * 用户：删除我的报修任务（仅待审核状态可删除）
     */
    @DeleteMapping("/my/{id}")
    public Result deleteMyMaintenance(@PathVariable Integer id) {
        maintenanceRecordService.deleteMyMaintenance(id);
        return Result.success();
    }
    
    /**
     * 用户：获取我的报修进度统计
     */
    @GetMapping("/my/progress")
    public Result<Map<String, Integer>> getMyMaintenanceProgress() {
        Map<String, Integer> progress = maintenanceRecordService.getMyMaintenanceProgress();
        return Result.success(progress);
    }

    /**
     * 老师：分页查询待处理（指派给我，状态0/1）的维修记录
     */
    @GetMapping("/teacher/todo")
    public Result<PageBean<MaintenanceRecord>> listTodoAsTeacher(@RequestParam Integer pageNo,
                                                                  @RequestParam Integer pageSize) {
        PageBean<MaintenanceRecord> pageBean = maintenanceRecordService.listTodoAsTeacher(pageNo, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 管理员：分页查询全部维修记录
     */
    @GetMapping("/admin/list")
    public Result<PageBean<MaintenanceRecord>> listAll(@RequestParam Integer pageNo,
                                                       @RequestParam Integer pageSize) {
        PageBean<MaintenanceRecord> pageBean = maintenanceRecordService.listAll(pageNo, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 管理员：指派维修教师
     */
    @PatchMapping("/admin/{id}/assign")
    public Result assignTeacher(@PathVariable Integer id, @RequestBody MaintenanceAssignRequest request) {
        maintenanceRecordService.assignTeacher(id, request.getTeacherId());
        return Result.success();
    }
    
    /**
     * 管理员：删除维修任务
     */
    @DeleteMapping("/admin/{id}")
    public Result deleteMaintenanceRecord(@PathVariable Integer id) {
        maintenanceRecordService.deleteMaintenanceRecord(id);
        return Result.success();
    }

    
    /**
     * 教师：开始维修
     */
    @PatchMapping("/teacher/{id}/start")
    public Result startRepair(@PathVariable Integer id) {
        maintenanceRecordService.startRepair(id);
        return Result.success();
    }

    /**
     * 教师：完成维修
     */
    @PatchMapping("/teacher/{id}/complete")
    public Result completeRepair(@PathVariable Integer id, @RequestBody MaintenanceCompleteRequest request) {
        maintenanceRecordService.completeRepair(id, request.getRepairResult(), request.getCost());
        return Result.success();
    }

    /**
     * 所有用户：全局汇总（待维护/进行中/已完成/不需要维护）
     */
    
    @GetMapping("/summary")
    public Result<MaintenanceSummary> summaryAll() {
        MaintenanceSummary summary = maintenanceRecordService.summaryAll();
        return Result.success(summary);
    }

    /**
     * 老师：我的汇总（仅统计指派给我的记录）
     */
    @GetMapping("/teacher/summary")
    public Result<MaintenanceSummary> summaryForTeacher() {
        MaintenanceSummary summary = maintenanceRecordService.summaryForTeacher();
        return Result.success(summary);
    }

    /**
     * 老师：标记不需要维护
     */
    @PatchMapping("/teacher/{id}/no-need")
    public Result markNoNeed(@PathVariable Integer id) {
        maintenanceRecordService.markNoNeed(id);
        return Result.success();
    }
    
    /**
     * 所有用户：查看今日申请信息（限制5条）
     */
    @GetMapping("/today")
    public Result<List<MaintenanceRecord>> listTodayApplications() {
        List<MaintenanceRecord> records = maintenanceRecordService.listTodayApplications(5);
        return Result.success(records);
    }
}
