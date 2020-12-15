package com.liang.dto;

import lombok.Data;

/**
 * @author LiangYonghui
 * @date 2020/10/14 21:56
 * @description
 */
@Data
public class AddCommentDto {
    private String content;
    private Long articleId;
    private Long parentId;
    private Long toUserId;
    private Long userId;
    private String observerName;   // 评论者的名字
}
