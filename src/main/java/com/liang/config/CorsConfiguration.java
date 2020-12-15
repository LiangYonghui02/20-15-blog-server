package com.liang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author LiangYonghui
 * @date 2020/10/10 18:04
 * @description 解决跨域
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowCredentials(false)
                        .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                        .allowedOrigins("*");
            }
        };
    }

}
