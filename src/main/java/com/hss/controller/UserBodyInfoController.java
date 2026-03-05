package com.hss.controller;

import com.hss.pojo.Result;
import com.hss.pojo.UserBodyInfo;
import com.hss.service.UserBodyInfoService;
import com.hss.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/userBodyInfo")
public class UserBodyInfoController {

    @Autowired
    private UserBodyInfoService userBodyInfoService;
    // 获取用户身体信息
    @GetMapping
    public Result<UserBodyInfo> getUserBodyInfo() {
        UserBodyInfo userBodyInfo = userBodyInfoService.getUserBodyInfo();
        return Result.success(userBodyInfo);
    }
    // 更新用户身体信息
    @PostMapping
    public Result updateUserBodyInfo(@Validated @RequestBody UserBodyInfo userBodyInfo) {
        userBodyInfoService.updateUserBodyInfo(userBodyInfo);
        return Result.success(userBodyInfoService.getHealthAdvice(userBodyInfo));
    }

}
