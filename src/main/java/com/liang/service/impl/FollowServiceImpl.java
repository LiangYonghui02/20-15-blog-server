package com.liang.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liang.common.lang.Result;
import com.liang.dto.FollowDto;
import com.liang.entity.Article;
import com.liang.entity.Message;
import com.liang.entity.User;
import com.liang.mapper.MessageMapper;
import com.liang.mapper.UserMapper;
import com.liang.service.FollowService;
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
 * @author LiangYonghui
 * @date 2020/11/8 9:46
 * @description
 */
@Service
@Transactional
public class FollowServiceImpl implements FollowService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public Result follow(FollowDto followDto) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(followDto.getUserId(), followDto.getEntityType());
                String followerKey = RedisKeyUtil.getFollowerKey(followDto.getEntityType(), followDto.getEntityId());

                operations.multi();

                operations.opsForZSet().add(followeeKey, followDto.getEntityId(), System.currentTimeMillis());
                operations.opsForZSet().add(followerKey, followDto.getUserId(), System.currentTimeMillis());

                return operations.exec();
            }
        });

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // 写入通知表
                Message message = new Message();
                message.setFromId(followDto.getUserId());
                message.setToId(followDto.getEntityId());
                message.setStatus(0);
                // 用于鉴定是收藏还是别的什么
                message.setConversationId(BlogConstant.TOPIC_FOLLOW);
                message.setCreateTime(new Date());

                Map<String, Object> content = new HashMap<>();


                   // 收藏发起者
                User user = userMapper.selectById(followDto.getUserId());

                content.put("followerName", user.getNickname());

                message.setContent(JSONObject.toJSONString(content));

                System.out.println( "---------------------" +message + "---------------------");

                messageMapper.insert(message);
            }
        };
        executorService.submit(runnable);
        return Result.success();
    }

    @Override
    public Result unfollow(FollowDto followDto) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(followDto.getUserId(), followDto.getEntityType());
                String followerKey = RedisKeyUtil.getFollowerKey(followDto.getEntityType(), followDto.getEntityId());

                operations.multi();

                operations.opsForZSet().remove(followeeKey, followDto.getEntityId());
                operations.opsForZSet().remove(followerKey, followDto.getUserId());

                return operations.exec();
            }
        });
        return Result.success();
    }

    @Override
    public Result hasFollowed(FollowDto followDto) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(followDto.getUserId(), followDto.getEntityType());
        Double score = redisTemplate.opsForZSet().score(followeeKey, followDto.getEntityId());
        Map<String,Boolean> map = new HashMap<>();
        if (score != null) {
            map.put("hasFollowed",true);
        } else {
            map.put("hasFollowed",false);
        }
        return Result.success(map);
    }


    // 查询关注的实体的数量
    @Override
    public long findFolloweeCount(Long userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    // 查询实体的粉丝的数量
    @Override
    public long findFollowerCount(int entityType, Long entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }



}
