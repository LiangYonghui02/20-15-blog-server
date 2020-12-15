package com.liang.service;

import com.liang.common.lang.Result;
import com.liang.dto.ArticleDto;
import com.liang.dto.ReadDto;
import com.liang.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
public interface ArticleService extends IService<Article> {

    Result publish(ArticleDto article,String writer);

    Result findEssayById(Long id);

    Result listArticles();

    Result addRead(ReadDto readDto);

    int findEntityReadCount(Integer entityType, Long entityId);

    int findEntityReadStatus(Long userId, Integer entityType, Long entityId);

    Result isRead(ReadDto readDto);

    Result getReadCount(int entityTypeArticle, long parseLong);

    Article findLastArticle(Long userId);
}
