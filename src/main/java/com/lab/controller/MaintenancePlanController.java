package com.lab.controller;

import com.lab.pojo.MaintenancePlan;
import com.lab.pojo.PageBean;
import com.lab.pojo.Result;
import com.lab.service.MaintenancePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maintenance-plan")
@Validated
public class MaintenancePlanController {

    @Autowired
    private MaintenancePlanService maintenancePlanService;

    /** 管理员：分页查询维护计划 */
    @GetMapping("/admin/list")
    public Result<PageBean<MaintenancePlan>> list(@RequestParam Integer pageNo,
                                                  @RequestParam Integer pageSize) {
        PageBean<MaintenancePlan> pageBean = maintenancePlanService.list(pageNo, pageSize);
        return Result.success(pageBean);
    }

    /** 管理员：创建维护计划 */
    @PostMapping("/admin")
    public Result<MaintenancePlan> create(@RequestBody MaintenancePlan plan) {
        MaintenancePlan created = maintenancePlanService.create(plan);
        return Result.success(created);
    }

    /** 管理员：更新维护计划 */
    @PutMapping("/admin")
    public Result update(@RequestBody MaintenancePlan plan) {
        maintenancePlanService.update(plan);
        return Result.success();
    }

    /** 管理员：删除维护计划 */
    @DeleteMapping("/admin/{id}")
    public Result delete(@PathVariable Integer id) {
        maintenancePlanService.delete(id);
        return Result.success();
    }

    /** 管理员：启用/停用维护计划 */
    @PatchMapping("/admin/{id}/status")
    public Result updateStatus(@PathVariable Integer id, @RequestParam Integer status) {
        maintenancePlanService.updateStatus(id, status);
        return Result.success();
    }

    /** 按实验室查询启用的维护计划 */
    @GetMapping("/laboratory/{laboratoryId}/enabled")
    public Result<List<MaintenancePlan>> listEnabledByLab(@PathVariable Integer laboratoryId) {
        List<MaintenancePlan> list = maintenancePlanService.listEnabledByLaboratoryId(laboratoryId);
        return Result.success(list);
    }
}
