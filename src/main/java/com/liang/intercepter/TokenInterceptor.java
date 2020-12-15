package com.liang.intercepter;

import cn.hutool.core.util.StrUtil;
import com.liang.annotation.TokenFree;
import com.liang.exception.CustomException;
import com.liang.shiro.JwtToken;
import com.liang.utils.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author LiangYonghui
 * @date 2020/10/11 8:40
 * @description
 */
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            System.out.println("拦截了 : "+((HandlerMethod) handler).getMethod().getName());
            TokenFree tokenFree = handlerMethod.getMethodAnnotation(TokenFree.class);
            //无需token验证，直接放行
            if (tokenFree != null){

                return true;
            }
            //获取请求头里的token
            String token = request.getHeader(JwtUtil.HEADER_TOKEN_KEY);

            System.out.println("token---->" + token);

            if (StrUtil.isBlank(token)){
                System.out.println("00000000000000000000000000");
                throw new CustomException("请先登录");
            }
            //交给shiro验证token是否正确
            try {
                SecurityUtils.getSubject().login(new JwtToken(token));
            }catch (CustomException e){
                throw e;
            }
            return true;
        }
        return true;
    }
}
