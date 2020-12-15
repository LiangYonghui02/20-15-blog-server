package com.liang.vo;

import com.liang.entity.Article;
import com.liang.entity.ArticleDetail;
import lombok.Data;

import java.sql.Blob;

/**
 * @author LiangYonghui
 * @date 2020/10/13 20:36
 * @description
 */
@Data
public class EssayVo {
    private Article article;
    private ArticleDetail articleDetail;
    private String avatarUrl;
    private Integer collectCount; // 收藏数
}
