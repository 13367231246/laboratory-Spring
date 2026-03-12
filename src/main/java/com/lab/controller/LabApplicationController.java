package com.lab.controller;

import com.lab.pojo.LabApplication;
import com.lab.pojo.LabApplicationRequest;
import com.lab.pojo.PageBean;
import com.lab.pojo.Result;
import com.lab.pojo.UserStatistics;
import com.lab.service.LabApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/lab-application")
@Validated
public class LabApplicationController {

    @Autowired
    private LabApplicationService labApplicationService;

    /**
     * 普通用户：提交实验室申请
     * 前端仅传时间字符串（HH:mm:ss），后端自动拼接当天日期转换为 LocalDateTime
     */
    @PostMapping
    public Result<LabApplication> apply(@RequestBody LabApplicationRequest request) {
        LabApplication application = new LabApplication();
        application.setLaboratoryId(request.getLaboratoryId());
        application.setPurpose(request.getPurpose());
        application.setCourseName(request.getCourseName());
        application.setClassName(request.getClassName());
        application.setStudentCount(request.getStudentCount());
        application.setApplicantPhone(request.getApplicantPhone());
        application.setApplicantEmail(request.getApplicantEmail());

        LocalTime start = LocalTime.parse(request.getStartTime());
        LocalTime end = LocalTime.parse(request.getEndTime());
        LocalDate today = LocalDate.now();
        application.setStartTime(LocalDateTime.of(today, start));
        application.setEndTime(LocalDateTime.of(today, end));

        LabApplication created = labApplicationService.apply(application);
        return Result.success(created);
    }

    /**
     * 普通用户：撤销自己的申请（仅待审核、已批准可撤销）
     */
    @PatchMapping("/{id}/cancel")
    public Result cancel(@PathVariable Integer id) {
        labApplicationService.cancel(id);
        return Result.success();
    }

    /**
     * 普通用户：完成申请（仅已批准或使用中的申请可完成）
     */
    @PatchMapping("/{id}/finish")
    public Result finish(@PathVariable Integer id) {
        labApplicationService.finish(id);
        return Result.success();
    }

    /**
     * 普通用户：删除申请（仅已完成的申请可删除）
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        labApplicationService.delete(id);
        return Result.success();
    }

    /**
     * 管理员：审核通过申请
     */
    @PatchMapping("/admin/{id}/approve")
    public Result approve(@PathVariable Integer id,
                          @RequestParam(required = false) String reviewComment) {
        labApplicationService.approve(id, reviewComment);
        return Result.success();
    }

    /**
     * 管理员：审核拒绝申请（必须填写审核意见）
     */
    @PatchMapping("/admin/{id}/reject")
    public Result reject(@PathVariable Integer id,
                         @RequestParam String reviewComment) {
        labApplicationService.reject(id, reviewComment);
        return Result.success();
    }

    /**
     * 管理员：删除任意申请记录
     */
    @DeleteMapping("/admin/{id}")
    public Result adminDelete(@PathVariable Integer id) {
        labApplicationService.adminDelete(id);
        return Result.success();
    }

    /**
     * 管理员：分页查询所有申请记录
     */
    @GetMapping("/admin/list")
    public Result<PageBean<LabApplication>> listAll(@RequestParam Integer pageNo,
                                                    @RequestParam Integer pageSize) {
        PageBean<LabApplication> pageBean = labApplicationService.listAll(pageNo, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 普通用户：分页查询我的申请记录
     */
    @GetMapping("/my")
    public Result<PageBean<LabApplication>> listMyApplications(@RequestParam Integer pageNo,
                                                                @RequestParam Integer pageSize) {
        PageBean<LabApplication> pageBean = labApplicationService.listMyApplications(pageNo, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 普通用户：获取申请详情（含时间等记录）
     */
    @GetMapping("/{id}")
    public Result<LabApplication> getDetail(@PathVariable Integer id) {
        LabApplication application = labApplicationService.getDetail(id);
        return Result.success(application);
    }
    
    /**
     * 普通用户：获取我的统计信息（申请次数、报修次数、维修数、使用实验室数）
     */
    @GetMapping("/my/statistics")
    public Result<UserStatistics> getMyStatistics() {
        UserStatistics statistics = labApplicationService.getMyStatistics();
        return Result.success(statistics);
    }
    
    /**
     * 所有用户：查看今日实验室申请信息（限制5条）
     */
    @GetMapping("/today")
    public Result<List<LabApplication>> listTodayApplications() {
        List<LabApplication> applications = labApplicationService.getTodayApplications(5);
        return Result.success(applications);
    }
}
