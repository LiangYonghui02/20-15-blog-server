package com.liang.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Map;

/**
 * @author LiangYonghui
 * @date 2020/10/11 8:58
 * @description
 */
@Slf4j
public class JwtUtil {
    public static final String HEADER_TOKEN_KEY = "Authorization";

    /**
     *
     * @param map :
     * @param secret :
     * @return
     */
    public static String getToken(Map<String,String> map, String secret){
        JWTCreator.Builder builder = JWT.create();
        System.out.println("==============="+map.get("nickname"));
        map.forEach((k,v)->{
            log.info("JwtUtil key --> " + k);
            log.info("JwtUtil value --> " + v);
            builder.withClaim(k,v);
        });
        Calendar instance = Calendar.getInstance();
        //过期时间为2小时
        instance.add(Calendar.SECOND,60*60*2);
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(secret));
    }
    /**
     * 验证token
     * @param token
     * @return
     */
    public static void verify(String token){
        String secret= getToken(token).getClaim("email").asString();
        System.out.println("secrret-->" + secret);
        if (secret == null){
            secret = "as";
        }
        JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }


    /**
     * 获取token中payload,无需解密也可获得
     * @param token
     * @return
     */
    public static DecodedJWT getToken(String token){
        return JWT.decode(token);
    }
}
