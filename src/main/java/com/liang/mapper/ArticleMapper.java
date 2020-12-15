package com.liang.mapper;

import com.liang.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    Article findLastArticle(@Param("userId") Long userId);
}
