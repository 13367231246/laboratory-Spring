package com.hss.controller;

import com.hss.pojo.Laboratory;
import com.hss.pojo.PageBean;
import com.hss.pojo.Result;
import com.hss.service.LaboratoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * 根据ID获取实验室详情
     */
    @GetMapping("/{id}")
    public Result<Laboratory> getById(@PathVariable Integer id) {
        Laboratory laboratory = laboratoryService.getById(id);
        return Result.success(laboratory);
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

