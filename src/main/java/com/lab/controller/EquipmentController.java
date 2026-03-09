package com.lab.controller;

import com.lab.pojo.Equipment;
import com.lab.pojo.PageBean;
import com.lab.pojo.Result;
import com.lab.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipment")
@Validated
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    /**
     * 分页获取所有设备
     */
    @GetMapping("/list")
    public Result<PageBean<Equipment>> list(@RequestParam Integer pageNo,
                                           @RequestParam Integer pageSize) {
        PageBean<Equipment> pageBean = equipmentService.list(pageNo, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 不分页查询设备列表，可根据设备名模糊搜索
     */
    @GetMapping("/listAll")
    public Result<java.util.List<Equipment>> listAll(@RequestParam(required = false) String equipmentName) {
        java.util.List<Equipment> list = equipmentService.listAll(equipmentName);
        return Result.success(list);
    }

    /**
     * 根据ID获取设备详情
     */
    @GetMapping("/{id}")
    public Result<Equipment> getById(@PathVariable Integer id) {
        Equipment equipment = equipmentService.getById(id);
        return Result.success(equipment);
    }

    /**
     * 新增设备（仅管理员）
     */
    @PostMapping
    public Result create(@RequestBody Equipment equipment) {
        equipmentService.create(equipment);
        return Result.success();
    }

    /**
     * 更新设备信息（仅管理员）
     */
    @PutMapping
    public Result update(@RequestBody Equipment equipment) {
        equipmentService.update(equipment);
        return Result.success();
    }

    /**
     * 删除设备（仅管理员）
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        equipmentService.delete(id);
        return Result.success();
    }
}

