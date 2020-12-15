package com.liang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liang.common.lang.Result;
import com.liang.entity.Article;
import com.liang.entity.Blog;
import com.liang.entity.UserInfo;
import com.liang.mapper.ArticleMapper;
import com.liang.mapper.BlogMapper;
import com.liang.mapper.StatisInfoMapper;
import com.liang.mapper.UserInfoMapper;
import com.liang.service.*;
import com.liang.utils.BlogConstant;
import com.liang.utils.RedisKeyUtil;
import com.liang.vo.FollowUserInfoVo;
import com.liang.vo.MineArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author LiangYonghui
 * @date 2020/10/12 20:59
 * @description
 */
@Service
@Transactional
public class MineArticleImpl implements MineService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticlePollService articlePollService;

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public Result getMineArticle(Long userId, String nickname) {
        System.out.println("产寻");
        // 查询文章
        QueryWrapper<Article> articleWrapper = new QueryWrapper<>();
        articleWrapper.eq("user_id",userId).orderByDesc("create_time");

        List<Article> articles = articleMapper.selectList(articleWrapper);
        // 设置阅读数和点赞数
        for (Article article : articles) {
            int readCount = articleService.findEntityReadCount(BlogConstant.ENTITY_TYPE_ARTICLE, article.getId());
            int likeCount = articlePollService.findEntityLikeCount(BlogConstant.ENTITY_TYPE_ARTICLE,article.getId());
            article.setPollCount(likeCount);
            article.setReadCount(readCount);
        }

        // 查询博客地址
        QueryWrapper<Blog> blogWrapper = new QueryWrapper<>();
        blogWrapper.eq("user_id", userId);
        Blog blog = blogMapper.selectOne(blogWrapper);

        // 封装数据
        MineArticleVo mineArticleVo = new MineArticleVo();
        mineArticleVo.setArticles(articles);
        mineArticleVo.setWriter(nickname);
        mineArticleVo.setBlogUrl(blog.getBlogUrl());

        return Result.success(mineArticleVo);
    }


    @Override
    public Result mineArticle(Long userId) {
        // 查询文章
        QueryWrapper<Article> articleWrapper = new QueryWrapper<>();
        articleWrapper.eq("user_id",userId);
        List<Article> articles = articleMapper.selectList(articleWrapper);

        // 查询博客地址
        QueryWrapper<Blog> blogWrapper = new QueryWrapper<>();
        blogWrapper.eq("user_id", userId);
        Blog blog = blogMapper.selectOne(blogWrapper);

        // 封装数据
        MineArticleVo mineArticleVo = new MineArticleVo();
        mineArticleVo.setArticles(articles);
        mineArticleVo.setBlogUrl(blog.getBlogUrl());


        return Result.success(mineArticleVo);
    }

    @Override
    public Result getCollectArticles(Long id) {

        String userArticleCollectKey = RedisKeyUtil.getUserArticleCollectKey(id);
        Set<Long> articleIds = redisTemplate.opsForZSet().reverseRange(userArticleCollectKey,0,-1);

        List<Article> articles = new ArrayList<>();
        for (Long articleId : articleIds) {
            Article article = articleService.getById(articleId);
            int readCount = articleService.findEntityReadCount(BlogConstant.ENTITY_TYPE_ARTICLE, article.getId());
            int likeCount = articlePollService.findEntityLikeCount(BlogConstant.ENTITY_TYPE_ARTICLE,article.getId());
            article.setPollCount(likeCount);
            article.setReadCount(readCount);
            articles.add(article);
        }
        System.out.println("收藏的------>"+articles);

        return Result.success(articles);
    }

    @Override
    public Result getFollowUsers(Long userId) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, BlogConstant.ENTITY_TYPE_USER);
        Set<Long> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, 0, - 1);


        if (targetIds == null) {
            return null;
        }

        List<FollowUserInfoVo> followUserInfoVos = new ArrayList<>();
        for (Long targetId : targetIds) {
            System.out.println("targetId----->"+targetId);
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id",targetId);
            UserInfo userInfo = userInfoService.getOne(wrapper);
            FollowUserInfoVo followUserInfoVo = new FollowUserInfoVo();

            followUserInfoVo.setAvatarUrl(userInfo.getAvatarUrl());
            followUserInfoVo.setId(userInfo.getId());
            followUserInfoVo.setEmail(userInfo.getEmail());
            followUserInfoVo.setIntro(userInfo.getIntro());
            followUserInfoVo.setUserId(userInfo.getUserId());
            followUserInfoVo.setNickname(userInfo.getNickname());
            followUserInfoVo.setSignature(userInfo.getSignature());


            // 查询最近发表的文章的标题和id
            Article article = articleService.findLastArticle(targetId);
            followUserInfoVo.setArticleId(article.getId());
            followUserInfoVo.setLastArticleName(article.getTitle());

            // 查询文章数量
            QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",targetId);
            int count = articleService.count(queryWrapper);
            followUserInfoVo.setArticleCount(count);


            followUserInfoVos.add(followUserInfoVo);
        }

        System.out.println("getFollowUsers---------->==================="+ followUserInfoVos +"===================");
        return Result.success(followUserInfoVos);
    }
}
