package com.hss.config;

import com.hss.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //也要注入到容器 因为是配置类所有注解不同
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器 再设置放行的接口 登录和注册放行
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/login","/user/register");
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
