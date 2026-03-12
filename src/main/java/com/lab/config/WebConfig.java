package com.lab.config;

import com.lab.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //也要注入到容器 因为是配置类所有注解不同
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器，再设置放行的接口：用户登录/注册 + 管理员登录
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/admin/login",
                        "/notice/user/published",
                        "/maintenance/today",
                        "/laboratory/summary"
                );
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
//                .allowCredentials(true)
//                .maxAge(3600);
//    }
}
