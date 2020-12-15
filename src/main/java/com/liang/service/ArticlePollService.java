package com.liang.service;

import com.liang.common.lang.Result;
import com.liang.dto.ArticlePollDto;
import com.liang.entity.ArticlePoll;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
public interface ArticlePollService extends IService<ArticlePoll> {

    Result poll(Long userId, Long articleId);

    Result unPoll(Long userId, Long articleId);

    Result hasPoll(Long userId, Long articleId);

    Result poll(ArticlePollDto articlePollDto);

    int findEntityLikeCount(Integer entityType, Long entityId);

    int findEntityLikeStatus(Long userId, Integer entityType, Long entityId);



}
