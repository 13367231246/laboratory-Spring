package com.hss.service.impl;

import com.hss.mapper.EquipmentMapper;
import com.hss.pojo.Equipment;
import com.hss.pojo.PageBean;
import com.hss.service.EquipmentService;
import com.hss.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public PageBean<Equipment> list(Integer pageNo, Integer pageSize) {
        int offset = (pageNo - 1) * pageSize;
        List<Equipment> rows = equipmentMapper.findPage(offset, pageSize);
        Long total = equipmentMapper.countAll();

        PageBean<Equipment> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }

    @Override
    public List<Equipment> listAll(String equipmentName) {
        return equipmentMapper.findAll(equipmentName);
    }

    @Override
    public Equipment getById(Integer id) {
        return equipmentMapper.findById(id);
    }

    @Override
    public void create(Equipment equipment) {
        // 校验管理员权限
        getCurrentAdminId();

        // 检查资产编号是否已存在
        if (equipment.getAssetNumber() != null) {
            Equipment existing = equipmentMapper.findByAssetNumber(equipment.getAssetNumber());
            if (existing != null) {
                throw new RuntimeException("设备资产编号已存在，请使用其他编号");
            }
        }

        // 设置默认值
        if (equipment.getQuantity() == null) {
            equipment.setQuantity(1);
        }
        if (equipment.getAvailableQuantity() == null) {
            equipment.setAvailableQuantity(equipment.getQuantity());
        }
        if (equipment.getStatus() == null) {
            equipment.setStatus(0);
        }

        equipmentMapper.add(equipment);
    }

    @Override
    public void update(Equipment equipment) {
        // 校验管理员权限
        getCurrentAdminId();

        // 如果修改了资产编号，检查新编号是否已被其他设备使用
        Equipment existing = equipmentMapper.findById(equipment.getId());
        if (existing == null) {
            throw new RuntimeException("设备不存在");
        }

        if (equipment.getAssetNumber() != null && !equipment.getAssetNumber().equals(existing.getAssetNumber())) {
            Equipment existingByAssetNumber = equipmentMapper.findByAssetNumber(equipment.getAssetNumber());
            if (existingByAssetNumber != null && !existingByAssetNumber.getId().equals(equipment.getId())) {
                throw new RuntimeException("设备资产编号已被其他设备使用，请使用其他编号");
            }
        }

        equipmentMapper.update(equipment);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        // 校验管理员权限
        getCurrentAdminId();

        // 删除设备
        equipmentMapper.delete(id);
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

