package com.liang.dto;

import lombok.Data;

/**
 * @author LiangYonghui
 * @date 2020/11/9 18:37
 * @description
 */
@Data
public class GetMsgDto {
    private Long oppositeId; // 对方的Id
    private Long userId;   // 自己的id
}
