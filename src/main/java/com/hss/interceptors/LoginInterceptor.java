package com.hss.interceptors;

import com.hss.utils.JwtUtil;
import com.hss.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component //这个注解的意思是将这个拦截器的对象注入到ioc容器 方便后续注册拦截器 同@Service Mapper
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证 首先要得到token 前面都是利用什么变量claims的方式得到token 这里我们利用request
        //因为所有的请求数据都在request里面
        String token = request.getHeader("Authorization");//获取请求头的token
        //验证token
        try {
            //从redis中获取相同的token
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String redisToken = operations.get(token);
            if(redisToken==null){
                //token已经失效 抛出异常
                throw new RuntimeException();
            }
            Map<String,Object> claims = JwtUtil.parseToken(token);// 要验证失败就用这段代码抛出异常 用异常处理函数
            //把业务数据存储到ThreadLocal中
            ThreadLocalUtil.set(claims);

            return true;//这里就不是返回数据了 而是放行
        } catch (Exception e) {
            //http响应状态码为401
            response.setStatus(401);
            return false; //不放行
        }
    }
    //请求结束 移除数据 防止内存泄露
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}
