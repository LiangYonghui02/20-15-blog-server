package com.liang.shiro;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.liang.exception.CustomException;
import com.liang.exception.CustomExceptionCode;
import com.liang.mapper.UserMapper;
import com.liang.utils.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * @author LiangYonghui
 * @date 2020/10/11 11:13
 * @description
 */
public class UserRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        //获取用户的id
//        int id = (int) principalCollection.getPrimaryPrincipal();
//        //根据用户名查询当前用户的角色列表
//        Set<String> roleNames = roleDao.queryRoleNamesByUserId(id);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.setRoles(roleNames);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws CustomException,AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;

        //验证token
        try {
            JwtUtil.verify(jwtToken.getCredentials());
        }catch (SignatureVerificationException e){
            //签名不一致异常
            throw new CustomException(CustomExceptionCode.TOKEN_ERROR);
        }catch (TokenExpiredException e){
            //令牌过期异常
            throw new CustomException(CustomExceptionCode.TOKNE_EXPIRED);
        }catch (AlgorithmMismatchException e){
            //算法不匹配异常
            throw new CustomException(CustomExceptionCode.TOKEN_ERROR);
        }catch (InvalidClaimException e){
            //失效的payload异常
            throw new CustomException(CustomExceptionCode.TOKEN_ERROR);
        }catch (Exception e){
            throw new CustomException(CustomExceptionCode.TOKEN_ERROR);
        }

        Long id = Long.parseLong(JwtUtil.getToken(jwtToken.getCredentials()).getClaim("id").asString());

        //缓存用户名
        return new SimpleAuthenticationInfo(id,jwtToken.getCredentials(),getName());
    }
}
