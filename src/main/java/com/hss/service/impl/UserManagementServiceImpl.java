package com.hss.service.impl;

import com.hss.mapper.UserManagementMapper;
import com.hss.pojo.PageBean;
import com.hss.pojo.User;
import com.hss.service.UserManagementService;
import com.hss.utils.Md5Util;
import com.hss.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    private UserManagementMapper userManagementMapper;

    @Override
    public PageBean<User> listStudents(Integer pageNo, Integer pageSize) {
        int offset = (pageNo - 1) * pageSize;
        List<User> rows = userManagementMapper.findStudentsPage(offset, pageSize);
        Long total = userManagementMapper.countStudents();

        PageBean<User> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }

    @Override
    public PageBean<User> listTeachers(Integer pageNo, Integer pageSize) {
        int offset = (pageNo - 1) * pageSize;
        List<User> rows = userManagementMapper.findTeachersPage(offset, pageSize);
        Long total = userManagementMapper.countTeachers();

        PageBean<User> pageBean = new PageBean<>();
        pageBean.setTotal(total);
        pageBean.setItems(rows);
        return pageBean;
    }

    @Override
    public User getById(Integer id) {
        return userManagementMapper.findById(id);
    }

    @Override
    public void addStudent(User user) {
        // 校验管理员权限
        getCurrentAdminId();
        
        // 检查学工号是否已存在
        if (user.getStudentId() != null) {
            User existingByStudentId = userManagementMapper.findByStudentId(user.getStudentId());
            if (existingByStudentId != null) {
                throw new RuntimeException("学工号已存在，请使用其他学工号");
            }
        }
        
        // 检查用户名是否已存在
        if (user.getUsername() != null) {
            User existingByUsername = userManagementMapper.findByUsername(user.getUsername());
            if (existingByUsername != null) {
                throw new RuntimeException("用户名已存在，请使用其他用户名");
            }
        }
        
        // 检查手机号是否已存在
        if (user.getPhone() != null) {
            User existingByPhone = userManagementMapper.findByPhone(user.getPhone());
            if (existingByPhone != null) {
                throw new RuntimeException("手机号已存在，请使用其他手机号");
            }
        }
        
        // 设置角色为学生
        user.setRole("student");
        
        // 设置默认密码为123456（MD5加密）
        String defaultPassword = Md5Util.getMD5String("123456");
        user.setPassword(defaultPassword);
        
        // 默认状态为正常
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        
        userManagementMapper.add(user);
    }

    @Override
    public void addTeacher(User user) {
        // 校验管理员权限
        getCurrentAdminId();
        
        // 检查学工号是否已存在
        if (user.getStudentId() != null) {
            User existingByStudentId = userManagementMapper.findByStudentId(user.getStudentId());
            if (existingByStudentId != null) {
                throw new RuntimeException("学工号已存在，请使用其他学工号");
            }
        }
        
        // 检查用户名是否已存在
        if (user.getUsername() != null) {
            User existingByUsername = userManagementMapper.findByUsername(user.getUsername());
            if (existingByUsername != null) {
                throw new RuntimeException("用户名已存在，请使用其他用户名");
            }
        }
        
        // 检查手机号是否已存在
        if (user.getPhone() != null) {
            User existingByPhone = userManagementMapper.findByPhone(user.getPhone());
            if (existingByPhone != null) {
                throw new RuntimeException("手机号已存在，请使用其他手机号");
            }
        }
        
        // 设置角色为老师
        user.setRole("teacher");
        
        // 设置默认密码为123456（MD5加密）
        String defaultPassword = Md5Util.getMD5String("123456");
        user.setPassword(defaultPassword);
        
        // 默认状态为正常
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        
        userManagementMapper.add(user);
    }

    @Override
    public void update(User user) {
        // 校验管理员权限
        getCurrentAdminId();
        
        // 确保不更新用户名（从数据库查询原用户名）
        User existing = userManagementMapper.findById(user.getId());
        if (existing == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setUsername(existing.getUsername()); // 保持原用户名不变
        
        // 如果修改了学工号，检查新学工号是否已被其他用户使用
        if (user.getStudentId() != null && !user.getStudentId().equals(existing.getStudentId())) {
            User existingByStudentId = userManagementMapper.findByStudentId(user.getStudentId());
            if (existingByStudentId != null && !existingByStudentId.getId().equals(user.getId())) {
                throw new RuntimeException("学工号已被其他用户使用，请使用其他学工号");
            }
        }
        
        // 如果修改了手机号，检查新手机号是否已被其他用户使用
        if (user.getPhone() != null && !user.getPhone().equals(existing.getPhone())) {
            User existingByPhone = userManagementMapper.findByPhone(user.getPhone());
            if (existingByPhone != null && !existingByPhone.getId().equals(user.getId())) {
                throw new RuntimeException("手机号已被其他用户使用，请使用其他手机号");
            }
        }
        
        userManagementMapper.update(user);
    }

    @Override
    public void resetPassword(Integer id) {
        // 校验管理员权限
        getCurrentAdminId();
        
        // 重置密码为123456（MD5加密）
        String defaultPassword = Md5Util.getMD5String("123456");
        userManagementMapper.resetPassword(id, defaultPassword);
    }

    @Override
    public void disable(Integer id) {
        // 校验管理员权限
        getCurrentAdminId();
        userManagementMapper.updateStatus(id, 0);
    }

    @Override
    public void enable(Integer id) {
        // 校验管理员权限
        getCurrentAdminId();
        userManagementMapper.updateStatus(id, 1);
    }

    @Override
    public void delete(Integer id) {
        // 校验管理员权限
        getCurrentAdminId();
        userManagementMapper.delete(id);
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

