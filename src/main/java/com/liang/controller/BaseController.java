package com.liang.controller;

import com.liang.service.UserInfoService;
import com.liang.service.UserService;
import com.liang.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LiangYonghui
 * @date 2020/10/10 14:37
 * @description
 */
public class BaseController {
    @Autowired
    HttpServletRequest req;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserService userService;

    public Long getUserId(HttpServletRequest req) {
        String token = req.getHeader(JwtUtil.HEADER_TOKEN_KEY);
        return Long.parseLong(JwtUtil.getToken(token).getClaim("id").asString());
    }


    public String getNickname(HttpServletRequest req) {
        String token = req.getHeader(JwtUtil.HEADER_TOKEN_KEY);
        return JwtUtil.getToken(token).getClaim("nickname").asString();
    }

}
