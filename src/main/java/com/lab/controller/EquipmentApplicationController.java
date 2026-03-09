package com.lab.controller;

import com.lab.pojo.EquipmentApplication;
import com.lab.pojo.EquipmentApplicationRequest;
import com.lab.pojo.PageBean;
import com.lab.pojo.Result;
import com.lab.service.EquipmentApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipment-application")
@Validated
public class EquipmentApplicationController {

    @Autowired
    private EquipmentApplicationService equipmentApplicationService;

    /**
     * 用户：提交设备申请
     */
    @PostMapping
    public Result<EquipmentApplication> apply(@RequestBody EquipmentApplicationRequest request) {
        EquipmentApplication application = new EquipmentApplication();
        application.setEquipmentId(request.getEquipmentId());
        application.setLaboratoryId(request.getLaboratoryId());
        application.setQuantity(request.getQuantity());
        application.setPurpose(request.getPurpose());
        application.setStartTime(request.getStartTime());
        application.setEndTime(request.getEndTime());
        EquipmentApplication created = equipmentApplicationService.apply(application);
        return Result.success(created);
    }

    /**
     * 用户：撤销自己的申请（仅待审核、已批准可撤销）
     */
    @PatchMapping("/{id}/cancel")
    public Result cancel(@PathVariable Integer id) {
        equipmentApplicationService.cancel(id);
        return Result.success();
    }

    /**
     * 用户：完成使用（仅已批准或使用中的申请可完成）
     */
    @PatchMapping("/{id}/finish")
    public Result finish(@PathVariable Integer id) {
        equipmentApplicationService.finish(id);
        return Result.success();
    }

    /**
     * 用户：删除申请记录（仅已完成或已取消的申请可删除）
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        equipmentApplicationService.delete(id);
        return Result.success();
    }

    /**
     * 用户：分页查询我的申请记录
     */
    @GetMapping("/my")
    public Result<PageBean<EquipmentApplication>> listMyApplications(@RequestParam Integer pageNo,
                                                                      @RequestParam Integer pageSize) {
        PageBean<EquipmentApplication> pageBean = equipmentApplicationService.listMyApplications(pageNo, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 用户：查看申请详情
     */
    @GetMapping("/{id}")
    public Result<EquipmentApplication> getDetail(@PathVariable Integer id) {
        EquipmentApplication application = equipmentApplicationService.getDetail(id);
        return Result.success(application);
    }

    /**
     * 管理员：审核通过
     */
    @PatchMapping("/admin/{id}/approve")
    public Result approve(@PathVariable Integer id,
                          @RequestParam(required = false) String reviewComment) {
        equipmentApplicationService.approve(id, reviewComment);
        return Result.success();
    }

    /**
     * 管理员：审核拒绝（必须填写审核意见）
     */
    @PatchMapping("/admin/{id}/reject")
    public Result reject(@PathVariable Integer id,
                         @RequestParam String reviewComment) {
        equipmentApplicationService.reject(id, reviewComment);
        return Result.success();
    }

    /**
     * 管理员：删除任意申请记录
     */
    @DeleteMapping("/admin/{id}")
    public Result adminDelete(@PathVariable Integer id) {
        equipmentApplicationService.adminDelete(id);
        return Result.success();
    }

    /**
     * 管理员：分页查询所有申请记录
     */
    @GetMapping("/admin/list")
    public Result<PageBean<EquipmentApplication>> listAll(@RequestParam Integer pageNo,
                                                          @RequestParam Integer pageSize) {
        PageBean<EquipmentApplication> pageBean = equipmentApplicationService.listAll(pageNo, pageSize);
        return Result.success(pageBean);
    }
}
