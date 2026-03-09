package com.lab.service.impl;

import com.lab.mapper.CollegeMapper;
import com.lab.pojo.College;
import com.lab.pojo.PageBean;
import com.lab.service.CollegeService;
import com.lab.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CollegeServiceImpl implements CollegeService {

    @Autowired
    private CollegeMapper collegeMapper;

    @Override
    public PageBean<College> list(Integer pageNo, Integer pageSize) {
        int offset = (pageNo - 1) * pageSize;
        List<College> rows = collegeMapper.findPage(offset, pageSize);
        Long total = collegeMapper.countAll();

        PageBean<College> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }

    @Override
    public List<College> findAll() {
        return collegeMapper.findAll();
    }

    @Override
    public College getById(Integer id) {
        return collegeMapper.findById(id);
    }

    @Override
    public void create(College college) {
        // 校验管理员权限
        getCurrentAdminId();

        // 检查学院代码是否已存在
        if (college.getCollegeCode() != null) {
            College existing = collegeMapper.findByCollegeCode(college.getCollegeCode());
            if (existing != null) {
                throw new RuntimeException("学院代码已存在，请使用其他代码");
            }
        }

        // 检查学院名称是否已存在
        if (college.getCollegeName() != null) {
            College existing = collegeMapper.findByCollegeName(college.getCollegeName());
            if (existing != null) {
                throw new RuntimeException("学院名称已存在，请使用其他名称");
            }
        }

        collegeMapper.add(college);
    }

    @Override
    public void update(College college) {
        // 校验管理员权限
        getCurrentAdminId();

        // 检查学院是否存在
        College existing = collegeMapper.findById(college.getId());
        if (existing == null) {
            throw new RuntimeException("学院不存在");
        }

        // 如果修改了学院代码，检查新代码是否已被其他学院使用
        if (college.getCollegeCode() != null && !college.getCollegeCode().equals(existing.getCollegeCode())) {
            College existingByCode = collegeMapper.findByCollegeCode(college.getCollegeCode());
            if (existingByCode != null && !existingByCode.getId().equals(college.getId())) {
                throw new RuntimeException("学院代码已被其他学院使用，请使用其他代码");
            }
        }

        // 如果修改了学院名称，检查新名称是否已被其他学院使用
        if (college.getCollegeName() != null && !college.getCollegeName().equals(existing.getCollegeName())) {
            College existingByName = collegeMapper.findByCollegeName(college.getCollegeName());
            if (existingByName != null && !existingByName.getId().equals(college.getId())) {
                throw new RuntimeException("学院名称已被其他学院使用，请使用其他名称");
            }
        }

        collegeMapper.update(college);
    }

    @Override
    public void delete(Integer id) {
        // 校验管理员权限
        getCurrentAdminId();

        // 检查学院是否存在
        College existing = collegeMapper.findById(id);
        if (existing == null) {
            throw new RuntimeException("学院不存在");
        }

        collegeMapper.delete(id);
    }

    /**
     * 从 ThreadLocal（token解析后的claims）中直接获取管理员ID
     */
    private Integer getCurrentAdminId() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer adminId = (Integer) claims.get("id");
        if (adminId == null) {
            throw new RuntimeException("无权限操作，仅管理员可执行该操作");
        }
        return adminId;
    }
}

