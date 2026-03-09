package com.lab.controller;

import com.lab.pojo.College;
import com.lab.pojo.PageBean;
import com.lab.pojo.Result;
import com.lab.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/college")
@Validated
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    /**
     * 分页获取所有学院
     */
    @GetMapping("/list")
    public Result<PageBean<College>> list(@RequestParam Integer pageNo,
                                          @RequestParam Integer pageSize) {
        PageBean<College> pageBean = collegeService.list(pageNo, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 获取所有学院（不分页，用于下拉选择）
     */
    @GetMapping("/all")
    public Result<List<College>> findAll() {
        List<College> colleges = collegeService.findAll();
        return Result.success(colleges);
    }

    /**
     * 根据ID获取学院详情
     */
    @GetMapping("/{id}")
    public Result<College> getById(@PathVariable Integer id) {
        College college = collegeService.getById(id);
        return Result.success(college);
    }

    /**
     * 新增学院（仅管理员）
     */
    @PostMapping
    public Result create(@RequestBody College college) {
        collegeService.create(college);
        return Result.success();
    }

    /**
     * 更新学院信息（仅管理员）
     */
    @PutMapping
    public Result update(@RequestBody College college) {
        collegeService.update(college);
        return Result.success();
    }

    /**
     * 删除学院（仅管理员）
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        collegeService.delete(id);
        return Result.success();
    }
}

