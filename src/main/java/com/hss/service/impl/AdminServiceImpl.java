package com.hss.service.impl;

import com.hss.mapper.AdminMapper;
import com.hss.pojo.Admin;
import com.hss.service.AdminService;
import com.hss.utils.Md5Util;
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