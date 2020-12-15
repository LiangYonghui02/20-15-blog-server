package com.liang.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liang.common.lang.Result;
import com.liang.dto.ArticleDto;
import com.liang.dto.ReadDto;
import com.liang.entity.*;
import com.liang.mapper.*;
import com.liang.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liang.service.CollectService;
import com.liang.service.StatisInfoService;
import com.liang.utils.BlogConstant;
import com.liang.utils.RedisKeyUtil;
import com.liang.vo.EssayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
@Service
@Transactional
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService, BlogConstant {


    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleDetailMapper articleDetailMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private CollectService collectService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StatisInfoMapper statisInfoMapper;



    @Override
    public Result publish(ArticleDto articleDto,String writer) {

        // 设置默认值
        Date date = new Date();
        Article article = new Article();
        article.setCreateTime(date);
        article.setCommentCount(0);
        article.setIsDel(0);
        article.setPollCount(0);
        article.setReadCount(0);
        article.setUpdateTime(date);
        article.setClassId(0L);
        article.setIsOpen(0);
        article.setTagId(0L);

        article.setWriter(writer);

        // 设置dto中的值
        article.setUserId(articleDto.getUserId());
        article.setTitle(articleDto.getTitle());
        // 截取前100字作为摘要
        String summary = StrUtil.sub(articleDto.getText(), 0, 100);
        article.setSummary(summary);
        articleMapper.insert(article);

        // 文章详情
        ArticleDetail articleDetail = new ArticleDetail();
        articleDetail.setArticleId(article.getId());
        articleDetail.setContent(articleDto.getContent());
        articleDetail.setText(articleDto.getText());
        articleDetailMapper.insert(articleDetail);

        // 设置作者所写字数
        QueryWrapper<StatisInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",articleDto.getUserId());
        StatisInfo statisInfo = statisInfoMapper.selectOne(wrapper);
        statisInfo.setWords(statisInfo.getWords() + articleDto.getText().length());
        statisInfoMapper.update(statisInfo,wrapper);


        return Result.success("文章发表成功！");
    }

    @Override
    public Result findEssayById(Long id) {
        System.out.println("eaasyId---->"+id);
        Article article = articleMapper.selectById(id);
        ArticleDetail articleDetail = articleDetailMapper.selectOne(new QueryWrapper<ArticleDetail>().eq("article_id", id));
        EssayVo essayVo = new EssayVo();

        // 查找文章阅读数
        int readCount = this.findEntityReadCount(BlogConstant.ENTITY_TYPE_READ, article.getId());
        article.setReadCount(readCount);

        essayVo.setArticle(article);
        essayVo.setArticleDetail(articleDetail);

        Long userId = article.getUserId();

        UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("user_id",userId));

        System.out.println(userInfo);
        essayVo.setAvatarUrl(userInfo.getAvatarUrl());

        // 设置收藏数
        Integer count = collectService.findEntityCollectCount(BlogConstant.ENTITY_TYPE_COLLECT, article.getId());
        essayVo.setCollectCount(count);

        return Result.success(essayVo);
    }

    @Override
    public Result listArticles() {
        return Result.success();
    }

    @Override
    public Result addRead(ReadDto readDto) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String entityReadKey = RedisKeyUtil
                        .getEntityReadKey(readDto.getEntityType(), readDto.getEntityId());
                String userReadKey = RedisKeyUtil.getUserReadKey(readDto.getEntityUserId());

                boolean isMember = redisOperations.opsForSet().isMember(entityReadKey, readDto.getUserId());

                System.out.println(isMember);
                System.out.println(readDto.getUserId());
                System.out.println(isMember);



                redisOperations.multi();

                if (isMember) {
                    redisOperations.opsForSet().remove(entityReadKey, readDto.getUserId());
                    redisOperations.opsForValue().decrement(userReadKey);
                } else {
                    redisOperations.opsForSet().add(entityReadKey, readDto.getUserId());
                    redisOperations.opsForValue().increment(userReadKey);
                }

                return redisOperations.exec();
            }
        });


        // 数量
        int readCount = this.findEntityReadCount(readDto.getEntityType(), readDto.getEntityId());
        System.out.println("||||||readCount--->" + readCount + "|||||");
        // 当前用户是否已读
        int readStatus = this.findEntityReadStatus(readDto.getUserId(), readDto.getEntityType(), readDto.getEntityId());

        System.out.println("||||||readStatus--->" + readStatus + "|||||");
        Map<String, Object> map = new HashMap<>();
        map.put("readCount",readCount);
        map.put("readStatus",readStatus);
        return Result.success(map);
    }

    @Override
    public int findEntityReadCount(Integer entityType, Long entityId) {
        String entityReadKey = RedisKeyUtil.getEntityReadKey(entityType,entityId);
        Long count = redisTemplate.opsForSet().size(entityReadKey);
        return count == null ? 0 : count.intValue();
    }

    @Override
    public int findEntityReadStatus(Long userId, Integer entityType, Long entityId) {
        System.out.println(userId);
        System.out.println(entityId);
        String entityReadKey = RedisKeyUtil.getEntityReadKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityReadKey, userId) ? 1 : 0;
    }

    @Override
    public Result isRead(ReadDto readDto) {
        Integer status = this.findEntityReadStatus(readDto.getUserId(), readDto.getEntityType(), readDto.getEntityId());
        Map<String,Object> map = new HashMap<>();
        map.put("readStatus",status);
        return Result.success(map);
    }

    @Override
    public Result getReadCount(int entityType, long entityId) {
        int readCount = this.findEntityReadCount(entityType, entityId);
        Map<String, Object> map = new HashMap<>();
        map.put("readCount",readCount);
        return Result.success(map);
    }

    @Override
    public Article findLastArticle(Long userId) {
        return articleMapper.findLastArticle(userId);
    }


}
