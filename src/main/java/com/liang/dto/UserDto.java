package com.liang.dto;

import com.liang.entity.User;
import lombok.Data;

/**
 * @author LiangYonghui
 * @date 2020/10/10 19:46
 * @description
 */
@Data
public class UserDto {
    private String nickname;
    private String email;
    private String password;
    private String vercode;
}
