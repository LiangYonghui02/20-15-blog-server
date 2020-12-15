package com.liang.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author LiangYonghui
 * @date 2020/10/10 13:57
 * @description
 */
@Configuration
@Data
public class MailConfig {
    @Value("${email.host}")
    private String host;

    @Value("${email.port}")
    private String port;

    @Value("${email.from}")
    private String from;

    @Value("${email.user}")
    private String user;

    @Value("${email.pass}")
    private String pass;

}
