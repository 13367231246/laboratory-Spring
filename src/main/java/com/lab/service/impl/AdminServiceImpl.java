package com.lab.service.impl;

import com.lab.mapper.AdminMapper;
import com.lab.pojo.Admin;
import com.lab.service.AdminService;
import com.lab.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin findByUsername(String username) {
        return adminMapper.findByUsername(username);
    }

    @Override
    public void updatePassword(Integer id, String newPwd) {
        String md5String = Md5Util.getMD5String(newPwd);
        adminMapper.updatePassword(id, md5String);
    }
}