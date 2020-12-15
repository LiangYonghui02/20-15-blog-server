package com.liang.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liang.common.lang.Result;
import com.liang.dto.ArticlePollDto;
import com.liang.entity.Article;
import com.liang.entity.ArticlePoll;
import com.liang.entity.Message;
import com.liang.entity.User;
import com.liang.mapper.ArticleMapper;
import com.liang.mapper.ArticlePollMapper;
import com.liang.mapper.MessageMapper;
import com.liang.mapper.UserMapper;
import com.liang.service.ArticlePollService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liang.utils.BlogConstant;
import com.liang.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
public class ArticlePollServiceImpl extends ServiceImpl<ArticlePollMapper, ArticlePoll> implements ArticlePollService, BlogConstant {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticlePollMapper articlePollMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;



    @Override
    public Result poll(Long userId, Long articleId) {
        // 增加文章点赞表的数据
        ArticlePoll articlePoll = new ArticlePoll();
        articlePoll.setArticleId(articleId);
        articlePoll.setTime(new Date());
        // 设置点赞人的id
        articlePoll.setUserId(userId);
        articlePollMapper.insert(articlePoll);

        Article article = articleMapper.selectById(articleId);
        // 增加点赞数
        article.setPollCount(article.getPollCount() + 1);
        articleMapper.updateById(article);
        System.out.println("+1后的点赞数--->" + article.getPollCount());
        Map<String,Integer> map = new HashMap<>();
        map.put("essayPollCount",article.getPollCount());
        return Result.success(map);
    }

    @Override
    public Result unPoll(Long userId, Long articleId) {
        // 查询出文章
        Article article = articleMapper.selectById(articleId);
        article.setPollCount(article.getPollCount() - 1);
        articleMapper.updateById(article);
        QueryWrapper<ArticlePoll> wrapper = new QueryWrapper<>();
        HashMap<String, Long> hashMap = new HashMap<>();
        hashMap.put("user_id",userId);
        hashMap.put("article_id",articleId);
        articlePollMapper.delete(wrapper.allEq(hashMap));
        HashMap<String, Integer> map = new HashMap<>();
        map.put("essayPollCount",article.getPollCount());
        return Result.success(map);
    }

    @Override
    public Result hasPoll(Long userId, Long articleId) {
        QueryWrapper<ArticlePoll> hasPollWrapper = new QueryWrapper<>();
        HashMap<String, Long> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("article_id",articleId);
        hasPollWrapper.allEq(map);
        ArticlePoll articlePoll = articlePollMapper.selectOne(hasPollWrapper);

        HashMap<String, Boolean> hasPollMap = new HashMap<>();
        if (ObjectUtil.isNull(articlePoll)) {
            hasPollMap.put("hasPoll",false);
            return Result.success(hasPollMap);
        }
        hasPollMap.put("hasPoll",true);
        return Result.success(hasPollMap);
    }

    @Override
    public Result poll(ArticlePollDto articlePollDto) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(BlogConstant.ENTITY_TYPE_ARTICLE
                        , articlePollDto.getEntityId());
                String userLikeKey = RedisKeyUtil.getUserLikeKey(articlePollDto.getEntityUserId());

                boolean isMember = operations.opsForSet().isMember(entityLikeKey, articlePollDto.getUserId());

                operations.multi();

                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, articlePollDto.getUserId());
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    operations.opsForSet().add(entityLikeKey, articlePollDto.getUserId());
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });

        Map<String, Object> map = new HashMap<>();

        // 数量
        int likeCount = this.findEntityLikeCount(articlePollDto.getEntityType(), articlePollDto.getEntityId());
        System.out.println("||||||likeCount--->" + likeCount + "|||||");
        // 状态
        int likeStatus = this.findEntityLikeStatus(articlePollDto.getUserId(), articlePollDto.getEntityType(), articlePollDto.getEntityId());

        System.out.println("||||||likeStatus--->" + likeStatus + "|||||");
        map.put("likeCount",likeCount);
        map.put("likeStatus",likeStatus);

        if (likeStatus == 1) {
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    // 写入通知表
                    // 发送站内通知
                    Message message = new Message();
                    message.setFromId(articlePollDto.getUserId());
                    message.setToId(articlePollDto.getEntityUserId());
                    message.setStatus(0);
                    // 用于鉴定是点赞还是别的什么
                    message.setConversationId(BlogConstant.TOPIC_LIKE);
                    message.setCreateTime(new Date());

                    Map<String, Object> content = new HashMap<>();


                    // 获取文章名
                    Article article = articleMapper.selectById(articlePollDto.getEntityId());
                    // 点赞发起者
                    User user = userMapper.selectById(articlePollDto.getUserId());
                    content.put("articleName", article.getTitle());
                    content.put("collectUser", user.getNickname());
                    // 实体id
                    content.put("entityId",articlePollDto.getEntityId());
                    content.put("entityType",articlePollDto.getEntityType());
                    message.setContent(JSONObject.toJSONString(content));

                    System.out.println( "---------------------" +message + "---------------------");

                    messageMapper.insert(message);
                }
            };
            executorService.submit(runnable);
        }

        return Result.success(map);

    }

    @Override
    public int findEntityLikeCount(Integer entityType, Long entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey).intValue();
    }

    @Override
    public int findEntityLikeStatus(Long userId, Integer entityType, Long entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }



}
