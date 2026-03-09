package com.lab.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lab.pojo.LabEquipmentItem;
import com.lab.pojo.Laboratory;
import com.lab.pojo.PageBean;
import com.lab.pojo.Result;
import com.lab.service.LaboratoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/laboratory")
@Validated
public class LaboratoryController {

    @Autowired
    private LaboratoryService laboratoryService;

    /**
     * 分页获取所有实验室
     */
    @GetMapping("/list")
    public Result<PageBean<Laboratory>> list(@RequestParam Integer pageNo,
                                            @RequestParam Integer pageSize) {
        PageBean<Laboratory> pageBean = laboratoryService.list(pageNo, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 根据ID获取实验室详情（含设备列表 equipmentList）
     */
    @GetMapping("/{id}")
    public Result<Laboratory> getById(@PathVariable Integer id) {
        Laboratory laboratory = laboratoryService.getById(id);
        return Result.success(laboratory);
    }

    /**
     * 普通用户：获取所有状态正常的可申请实验室（status=1，不分页，含设备列表）
     */
    @GetMapping("/user/available")
    public Result<List<Laboratory>> listAvailable() {
        List<Laboratory> list = laboratoryService.listAvailable();
        return Result.success(list);
    }

    /**
     * 根据实验室ID查询该实验室内设备列表
     */
    @GetMapping("/{id}/equipment")
    public Result<List<LabEquipmentItem>> getEquipmentByLaboratoryId(@PathVariable Integer id) throws JsonProcessingException {
        List<LabEquipmentItem> list = laboratoryService.getEquipmentByLaboratoryId(id);
        return Result.success(list);
    }

    /**
     * 新增实验室（仅管理员）
     */
    @PostMapping
    public Result create(@RequestBody Laboratory laboratory) {
        laboratoryService.create(laboratory);
        return Result.success();
    }

    /**
     * 更新实验室信息（仅管理员）
     */
    @PutMapping
    public Result update(@RequestBody Laboratory laboratory) {
        laboratoryService.update(laboratory);
        return Result.success();
    }

    /**
     * 删除实验室（仅管理员）
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        laboratoryService.delete(id);
        return Result.success();
    }

    /**
     * 添加设备到实验室（仅管理员）
     */
    @PostMapping("/{laboratoryId}/equipment")
    public Result addEquipment(@PathVariable Integer laboratoryId,
                              @RequestBody Map<String, Object> params) {
        Integer equipmentId = (Integer) params.get("equipmentId");
        Integer quantity = params.get("quantity") != null ? (Integer) params.get("quantity") : 1;
        laboratoryService.addEquipment(laboratoryId, equipmentId, quantity);
        return Result.success();
    }
}

