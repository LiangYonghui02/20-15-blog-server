package com.liang.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author LiangYonghui
 * @date 2020/10/11 9:49
 * @description
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public String getCredentials() {
        return token;
    }
}
