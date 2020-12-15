package com.liang.config;

import com.liang.intercepter.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author LiangYonghui
 * @date 2020/11/8 9:08
 * @description
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {




    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器，拦截所有访问路径
        registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**");

    }
}
