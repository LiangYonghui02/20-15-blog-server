package com.liang.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liang.common.lang.Result;
import com.liang.dto.CollectDto;
import com.liang.entity.Article;
import com.liang.entity.Event;
import com.liang.entity.Message;
import com.liang.entity.User;
import com.liang.mapper.ArticleMapper;
import com.liang.mapper.MessageMapper;
import com.liang.mapper.UserMapper;
import com.liang.service.ArticleService;
import com.liang.service.CollectService;
import com.liang.service.MessageService;
import com.liang.service.UserService;
import com.liang.utils.BlogConstant;
import com.liang.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author LiangYonghui
 * @date 2020/10/31 14:32
 * @description
 */
@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    MessageMapper messageMapper;


    @Override
    public Result collect(CollectDto collectDto) {
        System.out.println(collectDto + "==================");
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String entityCollectKey = RedisKeyUtil
                        .getEntityCollectKey(collectDto.getEntityType(), collectDto.getEntityId());
                String userCollectKey = RedisKeyUtil.getUserCollectKey(collectDto.getEntityUserId());
                String userArticleCollectKey = RedisKeyUtil.getUserArticleCollectKey(collectDto.getUserId());

                boolean isMember = redisOperations.opsForSet().isMember(entityCollectKey, collectDto.getUserId());

                redisOperations.multi();

                if (isMember) {
                    redisOperations.opsForSet().remove(entityCollectKey, collectDto.getUserId());
                    redisOperations.opsForValue().decrement(userCollectKey);
                } else {
                    System.out.println("userArticleCollectKey--->"+userArticleCollectKey);
                    redisOperations.opsForZSet().add(userArticleCollectKey, collectDto.getEntityId(),System.currentTimeMillis());
                    redisOperations.opsForSet().add(entityCollectKey, collectDto.getUserId());
                    redisOperations.opsForValue().increment(userCollectKey);
                }

                return redisOperations.exec();
            }
        });
        Map<String, Object> map = new HashMap<>();

        // 数量
        int collectCount = this.findEntityCollectCount(collectDto.getEntityType(), collectDto.getEntityId());
        System.out.println("||||||collectCount--->" + collectCount + "|||||");
        // 状态
        int collectStatus = this.findEntityCollectStatus(collectDto.getUserId(), collectDto.getEntityType(), collectDto.getEntityId());

        System.out.println("||||||collectStatus--->" + collectStatus + "|||||");
        map.put("collectCount", collectCount);
        map.put("collectStatus", collectStatus);

        if (collectStatus == 1) {
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    // 写入通知表
                    // 发送站内通知
                    Message message = new Message();
                    message.setFromId(collectDto.getUserId());
                    message.setToId(collectDto.getEntityUserId());
                    message.setStatus(0);
                    // 用于鉴定是收藏还是别的什么
                    message.setConversationId(BlogConstant.TOPIC_COLLECT);
                    message.setCreateTime(new Date());

                    Map<String, Object> content = new HashMap<>();


                    // 获取文章名
                    Article article = articleMapper.selectById(collectDto.getEntityId());
                    // 收藏发起者
                    User user = userMapper.selectById(collectDto.getUserId());
                    content.put("articleName", article.getTitle());
                    content.put("collectUser", user.getNickname());
                    // 实体id
                    content.put("entityId",collectDto.getEntityId());
                    content.put("entityType",collectDto.getEntityType());
                    message.setContent(JSONObject.toJSONString(content));

                    System.out.println( "---------------------" +message + "---------------------");

                    messageMapper.insert(message);
                }
            };
            executorService.submit(runnable);
        }

        return Result.success(map);
    }

    /**
     * 获取实体的收藏数
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public Integer findEntityCollectCount(Integer entityType, Long entityId) {
        String entityCollectKey = RedisKeyUtil.getEntityCollectKey(entityType, entityId);
        Long count = redisTemplate.opsForSet().size(entityCollectKey);
        return count == null ? 0 : count.intValue();
    }

    /**
     * 查看当前用户是否收藏此实体
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public Integer findEntityCollectStatus(Long userId, Integer entityType, Long entityId) {
        System.out.println(userId);
        System.out.println(entityId);
        String entityCollectKey = RedisKeyUtil.getEntityCollectKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityCollectKey, userId) ? 1 : 0;
    }

    @Override
    public Result isCollect(CollectDto collectDto) {
        Integer status = this.findEntityCollectStatus(collectDto.getUserId(), collectDto.getEntityType(), collectDto.getEntityId());
        Map<String, Object> map = new HashMap<>();
        map.put("collectStatus", status);
        return Result.success(map);
    }

    @Override
    public Result getCollectCount(int entityType, long entityId) {
        Integer collectCount = this.findEntityCollectCount(entityType, entityId);
        System.out.println("collectCount:-------------->" + collectCount);
        Map<String, Object> map = new HashMap<>();
        map.put("collectCount", collectCount);
        return Result.success(map);
    }
}
