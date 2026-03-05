package com.hss.service;

import com.hss.pojo.UserBodyInfo;

public interface UserBodyInfoService {
    // 根据用户id查询用户身体信息
    UserBodyInfo getUserBodyInfo();

    // 更新用户身体信息
    void updateUserBodyInfo(UserBodyInfo userBodyInfo);
    // 获取健康建议
    Object getHealthAdvice(UserBodyInfo userBodyInfo);
}
