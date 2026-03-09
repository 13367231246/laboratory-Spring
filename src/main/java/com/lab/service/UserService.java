package com.lab.service;

import com.lab.pojo.User;

public interface UserService {

    User findUserName(String username);

    User findByIdentifier(String identifier);

    User findByPhoneAndStudentId(String phone, String studentId);

    void register(User user);

    void updateInfo(User user);

    void updateAvatar(String avatarUrl);

    void updatePassword(String newPwd);

    void deleteUser();
}
