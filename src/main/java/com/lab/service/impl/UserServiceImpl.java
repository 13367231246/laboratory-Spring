package com.lab.service.impl;

import com.lab.mapper.UserMapper;
import com.lab.pojo.User;
import com.lab.service.UserService;
import com.lab.utils.Md5Util;
import com.lab.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserName(String username) {
        User u = userMapper.findUserName(username);
        return u;
    }

    @Override
    public User findByIdentifier(String identifier) {
        return userMapper.findByIdentifier(identifier);
    }

    @Override
    public User findByPhoneAndStudentId(String phone, String studentId) {
        return userMapper.findByPhoneAndStudentId(phone, studentId);
    }

    @Override
    public void register(User user) {
        // 密码加密处理 MD5 加密
        String md5String = Md5Util.getMD5String(user.getPassword());
        user.setPassword(md5String);
        // 默认真实姓名先用用户名，占位，后续由用户完善
        if (user.getRealName() == null || user.getRealName().isEmpty()) {
            user.setRealName(user.getUsername());
        }
        // 默认状态：1 正常
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        // 添加用户
        userMapper.add(user);
    }

    @Override
    public void updateInfo(User user) {
        //更新用户信息
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateInfo(user);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        //根据id更新头像
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updateAvatar(id,avatarUrl);
    }

    @Override
    public void updatePassword(String newPwd) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updatePassword(id,Md5Util.getMD5String(newPwd));

    }

    @Override
    public void deleteUser() {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.deleteUser(id);
    }
}
