package com.liang.dto;

import lombok.Data;

/**
 * @author LiangYonghui
 * @date 2020/10/12 17:57
 * @description
 */
@Data
public class ArticleDto {
    private String content; // html
    private String title;
    private Long userId;
    private String text; // 文本
}
