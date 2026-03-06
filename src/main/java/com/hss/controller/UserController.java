package com.hss.controller;


import com.hss.pojo.LoginRequest;
import com.hss.pojo.RegisterRequest;
import com.hss.pojo.Result;
import com.hss.pojo.User;
import com.hss.service.UserService;
import com.hss.utils.JwtUtil;
import com.hss.utils.Md5Util;
import com.hss.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 注册 post
    @PostMapping("/register")
//当你在处理HTTP请求时，尤其是POST或PUT请求，客户端通常会发送JSON或XML格式的数据。@RequestBody的作用就是将这些数据转换为Java对象。
//为什么使用 @RequestBody: 客户端发送一个HTTP请求（比如通过POST方法），并以JSON格式传递数据时，Spring需要一种方式来
//将这个JSON数据转换为你定义的Java对象。@RequestBody告诉Spring，"请将请求体中的数据映射到这个参数对象上"。
    public Result register(@Valid @RequestBody RegisterRequest request){
        String username = request.getUsername();
        String email = request.getEmail();
        String phone = request.getPhone();
        String role = request.getRole();
        String password = request.getPassword();
        String studentId = request.getStudentId();
        String realName = request.getRealName();
        //查询用户名是否占用
        User u = userService.findUserName(username);
        if(u != null){
            return Result.error("用户名已被占用");
        }
        
        //查询手机号是否已被注册
        User phoneUser = userService.findByIdentifier(phone);
        if(phoneUser != null){
            return Result.error("手机号已被注册");
        }
        
        //查询学工号是否已被注册
        User studentIdUser = userService.findByIdentifier(studentId);
        if(studentIdUser != null){
            return Result.error("学工号已被注册");
        }
        
        // 组装用户信息
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role);
        user.setPassword(password);
        user.setStudentId(studentId);
        // 默认真实姓名先用用户名，占位，后续由用户完善
        user.setRealName(realName);
        
        // 完成注册操作
        userService.register(user);
        return Result.success("注册成功");
    }

//    登录
    @PostMapping("/login")
    public Result login(@Valid @RequestBody LoginRequest request){
        String phone = request.getPhone();
        String studentId = request.getStudentId();
        String role = request.getRole();
        String password = request.getPassword();
        
        // 根据手机号和学工号同时查询用户
        User loginUser = userService.findByPhoneAndStudentId(phone, studentId);
        //判断用户是否存在
        if(loginUser == null){
            return Result.error("手机号/学工号或密码错误");
        }

        // 校验角色是否匹配
        if (loginUser.getRole() == null || !loginUser.getRole().equals(role)) {
            return Result.error("未找到对应角色");
        }

        // 检查用户状态，如果被禁用（status=0）则不允许登录
        if (loginUser.getStatus() != null && loginUser.getStatus() == 0) {
            return Result.error("账号已被禁用，请联系管理员");
        }
        
        //判断密码是否正确 密码是MD5加密的
        //可以对传进来的密码进行MD5加密再与数据库中的密码进行比对，成功返回用户token
        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            //登录成功 返回用户令牌token
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",loginUser.getId()); //存入用户id,值为loginUser.getId()获取到的id值
            claims.put("username",loginUser.getUsername()); //存入用户名,值为loginUser.getUsername()获取到的用户名值
            String token = JwtUtil.genToken(claims); //生成token
            //存入redis
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);//前面设置的token时间为1h
            return Result.success(token);
        }
        return Result.error("手机号/学工号或密码错误");
    }


    //获取用户详细信息
    @GetMapping("/userInfo")
    public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/){
        //根据用户名查询用户 用户名可以从请求头的token解析出来
       /*Map<String,Object> map = JwtUtil.parseToken(token);
       String username = (String) map.get("username");*/
        //从ThreadLocal中获取
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");

        User user = userService.findUserName(username);

        return Result.success(user);
    }

    // 更新用户信息（基础资料）
    @PutMapping("/updateInfo")
    public Result updateInfo(@Validated @RequestBody User user) {
        userService.updateInfo(user);
        return Result.success(1);
    }

    //更新用户头像
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    //更新用户密码
    @PatchMapping("/updatePwd")
    public Result updatePassword(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token) {
        //1.校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        //借助spring提供的方法判断不为空
        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要的参数");
        }
        // 检查新密码长度是否在5到16个字符之间
        if (newPwd.length() < 5 || newPwd.length() > 16) {
            return Result.error("新密码长度需在5到16个字符之间");
        }

        //原密码是否正确
        //调用userService根据用户名拿到原密码，再和用户输入的oldPwd加密后比对
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.findUserName(username);
        //判断旧密码是否正确
        if (!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error("原密码填写不正确");
        }
        //newPwd和rePwd是否一样
        if (!rePwd.equals(newPwd)) {
            return Result.error("密码不一致");
        }
        //2.修改密码调用service完成密码更新
        userService.updatePassword(newPwd);
        //4.删除redis中token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        //3.返回成功信息
        return Result.success("密码修改成功");
    }
    //注销

    @DeleteMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String token) {
        //删除redis中token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        //删除数据库的用户信息
        userService.deleteUser();
        return Result.success("注销成功");
    }
}
