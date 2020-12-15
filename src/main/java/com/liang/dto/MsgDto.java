package com.liang.dto;

import lombok.Data;

/**
 * @author LiangYonghui
 * @date 2020/11/9 17:06
 * @description
 */
@Data
public class MsgDto {
    private Long fromId;
    private Long toId;
    private String conversationId;
    private String content;
}
