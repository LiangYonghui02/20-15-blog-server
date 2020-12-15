package com.liang.common.excep;

import com.liang.common.lang.Result;
import com.liang.exception.CustomException;
import com.liang.exception.CustomExceptionCode;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author LiangYonghui
 * @date 2020/10/11 9:35
 * @description  通用全局异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public Result handleCustomException(CustomException e){
        System.out.println("1111111111111111111111");
        return Result.fail(e.getMessage());
    }


    /**
     * 处理自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    public Result handleShiroException(AuthenticationException e){
        //在userRealm抛出的异常，会被shiro框架转成AuthenticationException，它的cause才是我们原本抛出的异常
        if (e.getCause() instanceof  CustomException){
            return new Result((CustomException)e.getCause());
        }
        return new Result(CustomExceptionCode.SYSTEM_ERROR);
    }


    /**
     * 处理权限不足异常
     * @param e
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    public Result handleAuthorizationException(AuthorizationException e){
        return new Result(CustomExceptionCode.PERMISSION_ERROR);
    }


    @ExceptionHandler(UnavailableSecurityManagerException.class)
    public Result handleUnavailableSecurityManagerException(UnavailableSecurityManagerException e){
        System.out.println("-----不存在的路径---------");
        System.out.println(e.getMessage());
        return new Result(CustomExceptionCode.PATH_ERROR);
    }

    /**
     * 处理未知异常
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleSystemException(Exception e){
        System.out.println("-----未知异常--------------");
        e.printStackTrace();
        return new Result(CustomExceptionCode.SYSTEM_ERROR);
    }
}
