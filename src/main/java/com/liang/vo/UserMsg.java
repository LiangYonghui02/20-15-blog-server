package com.liang.vo;

import com.liang.entity.UserInfo;
import lombok.Data;



/**
 * @author LiangYonghui
 * @date 2020/10/12 9:23
 * @description
 */
@Data
public class UserMsg {
    private UserInfo userInfo;
    // 凭证
    private String jwtCert;
}
