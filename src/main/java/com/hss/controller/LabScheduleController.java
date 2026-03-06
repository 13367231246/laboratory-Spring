package com.hss.controller;

import com.hss.pojo.LabSchedule;
import com.hss.pojo.Result;
import com.hss.service.LabScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labSchedule")
@Validated
public class LabScheduleController {

    @Autowired
    private LabScheduleService labScheduleService;

    /**
     * 不分页查询时间规则（可按名称/类型/状态过滤）
     */
    @GetMapping("/listAll")
    public Result<List<LabSchedule>> listAll(@RequestParam(required = false) String ruleName,
                                             @RequestParam(required = false) String ruleType,
                                             @RequestParam(required = false) Integer status) {
        List<LabSchedule> list = labScheduleService.listAll(ruleName, ruleType, status);
        return Result.success(list);
    }

    /**
     * 根据ID查询规则详情
     */
    @GetMapping("/{id}")
    public Result<LabSchedule> getById(@PathVariable Integer id) {
        LabSchedule data = labScheduleService.getById(id);
        return Result.success(data);
    }

    /**
     * 新增规则（仅管理员）
     */
    @PostMapping
    public Result create(@RequestBody LabSchedule labSchedule) {
        labScheduleService.create(labSchedule);
        return Result.success();
    }

    /**
     * 更新规则（仅管理员）
     */
    @PutMapping
    public Result update(@RequestBody LabSchedule labSchedule) {
        labScheduleService.update(labSchedule);
        return Result.success();
    }

    /**
     * 删除规则（仅管理员）
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        labScheduleService.delete(id);
        return Result.success();
    }
}


