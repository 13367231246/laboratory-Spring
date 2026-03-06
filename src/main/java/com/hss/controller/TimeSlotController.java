package com.hss.controller;

import com.hss.pojo.Result;
import com.hss.pojo.TimeSlot;
import com.hss.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timeSlot")
@Validated
public class TimeSlotController {

    @Autowired
    private TimeSlotService timeSlotService;

    /**
     * 不分页查询时间段（可按名称/状态过滤）
     */
    @GetMapping("/listAll")
    public Result<List<TimeSlot>> listAll(@RequestParam(required = false) String slotName,
                                          @RequestParam(required = false) Integer status) {
        List<TimeSlot> list = timeSlotService.listAll(slotName, status);
        return Result.success(list);
    }

    /**
     * 根据ID查询时间段详情
     */
    @GetMapping("/{id}")
    public Result<TimeSlot> getById(@PathVariable Integer id) {
        TimeSlot timeSlot = timeSlotService.getById(id);
        return Result.success(timeSlot);
    }

    /**
     * 新增时间段（仅管理员）
     */
    @PostMapping
    public Result create(@RequestBody TimeSlot timeSlot) {
        timeSlotService.create(timeSlot);
        return Result.success();
    }

    /**
     * 更新时间段（仅管理员）
     */
    @PutMapping
    public Result update(@RequestBody TimeSlot timeSlot) {
        timeSlotService.update(timeSlot);
        return Result.success();
    }

    /**
     * 删除时间段（仅管理员）
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        timeSlotService.delete(id);
        return Result.success();
    }
}


