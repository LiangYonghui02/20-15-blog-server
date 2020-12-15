package com.liang.vo;

import com.liang.entity.Article;
import lombok.Data;

import java.util.List;

/**
 * @author LiangYonghui
 * @date 2020/10/12 20:06
 * @description  我的主页中我的文章列表
 */
@Data
public class MineArticleVo {
    private List<Article> articles;
    private String blogUrl;
    private String writer;
}
