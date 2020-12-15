package com.liang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liang.common.lang.Result;
import com.liang.entity.Article;
import com.liang.entity.ArticlePoll;
import com.liang.entity.StatisInfo;
import com.liang.entity.User;
import com.liang.mapper.ArticlePollMapper;
import com.liang.mapper.StatisInfoMapper;
import com.liang.mapper.UserMapper;
import com.liang.service.ArticleService;
import com.liang.service.FollowService;
import com.liang.service.StatisInfoService;
import com.liang.utils.BlogConstant;
import com.liang.utils.RedisKeyUtil;
import com.liang.vo.StatisInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LiangYonghui
 * @date 2020/10/13 13:12
 * @description
 */
@Service
@Transactional
public class StatisInfoServiceImpl extends ServiceImpl<StatisInfoMapper, StatisInfo> implements StatisInfoService {
    @Autowired
    private StatisInfoMapper statisInfoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FollowService followService;

    @Autowired
    private ArticleService articleService;

    @Override
    public Result getBaseInfo(Long id) {
        System.out.println(id);
        QueryWrapper<StatisInfo> statisInfoWrapper = new QueryWrapper<>();
        statisInfoWrapper.eq("user_id",id);
        StatisInfo statisInfo = statisInfoMapper.selectOne(statisInfoWrapper);
        User user = userMapper.selectById(id);

        // 设置用户所获赞数
        int userLikeCount = this.findUserLikeCount(id);
        statisInfo.setLikeCount(userLikeCount);


        // 设置用户的粉丝数
        long followerCount = followService.findFollowerCount(BlogConstant.ENTITY_TYPE_USER, id);
        statisInfo.setFans((int) followerCount);
        // 设置用户的关注量
        long followeeCount = followService.findFolloweeCount(id, BlogConstant.ENTITY_TYPE_USER);
        statisInfo.setAttention((int) followeeCount);

        // 设置文章数
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",id);
        int count = articleService.count(wrapper);
        statisInfo.setArticleCount(count);


        StatisInfoVo statisInfoVo = new StatisInfoVo();
        statisInfoVo.setStatisInfo(statisInfo);
        statisInfoVo.setUsername(user.getNickname());

        return Result.success(statisInfoVo);
    }

    // 查询某个用户获得的赞
    @Override
    public int findUserLikeCount(long userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count.intValue();
    }
}
