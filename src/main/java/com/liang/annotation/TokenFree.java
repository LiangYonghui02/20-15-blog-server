package com.liang.annotation;

import java.lang.annotation.*;

/**
 * @author LiangYonghui
 * @date 2020/10/11 8:43
 * @description token无需检查
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenFree {
}
