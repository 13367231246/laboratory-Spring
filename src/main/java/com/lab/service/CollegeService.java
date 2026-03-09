package com.lab.service;

import com.lab.pojo.College;
import com.lab.pojo.PageBean;

import java.util.List;

public interface CollegeService {

    /**
     * 分页获取所有学院
     */
    PageBean<College> list(Integer pageNo, Integer pageSize);

    /**
     * 获取所有学院（不分页，用于下拉选择）
     */
    List<College> findAll();

    /**
     * 根据ID获取学院详情
     */
    College getById(Integer id);

    /**
     * 新增学院
     */
    void create(College college);

    /**
     * 更新学院信息
     */
    void update(College college);

    /**
     * 删除学院
     */
    void delete(Integer id);
}

