package com.liang.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.liang.common.lang.Result;
import com.liang.dto.CommentPollDto;
import com.liang.entity.*;
import com.liang.mapper.*;
import com.liang.service.CommentPollService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
@Service
public class CommentPollServiceImpl extends ServiceImpl<CommentPollMapper, CommentPoll> implements CommentPollService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public Result addCommentPoll(CommentPollDto commentPollDto) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(commentPollDto.getEntityType()
                        , commentPollDto.getEntityId());
                String userLikeKey = RedisKeyUtil.getUserLikeKey(commentPollDto.getEntityUserId());

                boolean isMember = operations.opsForSet().isMember(entityLikeKey, commentPollDto.getUserId());

                operations.multi();

                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, commentPollDto.getUserId());
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    operations.opsForSet().add(entityLikeKey, commentPollDto.getUserId());
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });

        Map<String, Object> map = new HashMap<>();

        // 数量
        int likeCount = this.findEntityLikeCount(commentPollDto.getEntityType(), commentPollDto.getEntityId());
        System.out.println("||||||likeCount--->" + likeCount + "|||||");
        // 状态
        int likeStatus = this.findEntityLikeStatus(commentPollDto.getUserId(), commentPollDto.getEntityType(), commentPollDto.getEntityId());

        System.out.println("||||||likeStatus--->" + likeStatus + "|||||");
        map.put("likeCount",likeCount);
        map.put("likeStatus",likeStatus);

        if(likeStatus == 1) {
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    // 写入通知表
                    // 发送站内通知
                    Message message = new Message();
                    message.setFromId(commentPollDto.getUserId());
                    message.setToId(commentPollDto.getEntityUserId());
                    message.setStatus(0);
                    // 用于鉴定是收藏还是别的什么
                    message.setConversationId(BlogConstant.TOPIC_COLLECT);
                    message.setCreateTime(new Date());

                    Map<String, Object> content = new HashMap<>();


                    // 获取文章名
                    Comment comment = commentMapper.selectById(commentPollDto.getEntityId());
                    // 收藏发起者
                    User user = userMapper.selectById(commentPollDto.getUserId());


                    Article article = articleMapper.selectById(comment.getArticleId());
                    content.put("articleName", article.getTitle());
                    content.put("collectUser", user.getNickname());
                    // 实体id  这里设置的是文章的 虽然理论上来说是要设评论的
                    content.put("entityId",comment.getArticleId());
                    content.put("commentText",comment.getContent());
                    content.put("entityType",commentPollDto.getEntityType());
                    message.setContent(JSONObject.toJSONString(content));

                    System.out.println( "---------------------" +message + "---------------------");

                    messageMapper.insert(message);
                }
            };
            executorService.submit(runnable);
        }


        return Result.success(map);
    }

    // 查询某实体点赞的数量
    @Override
    public int findEntityLikeCount(Integer entityType, Long entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey).intValue();
    }

    // 查询某人对某实体的点赞状态
    @Override
    public int findEntityLikeStatus(Long userId, Integer entityType, Long entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }


}
