package com.liang.service;

import com.liang.common.lang.Result;
import com.liang.dto.CommentPollDto;
import com.liang.entity.CommentPoll;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
public interface CommentPollService extends IService<CommentPoll> {

    Result addCommentPoll(CommentPollDto commentPollDto);

    int findEntityLikeCount(Integer entityType, Long entityId);

    int findEntityLikeStatus(Long userId, Integer entityType, Long entityId);


}
