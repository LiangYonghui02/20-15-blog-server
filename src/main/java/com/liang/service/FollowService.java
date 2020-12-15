package com.liang.service;

import com.liang.common.lang.Result;
import com.liang.dto.FollowDto;

/**
 * @author LiangYonghui
 * @date 2020/11/8 9:46
 * @description
 */
public interface FollowService {
    Result follow(FollowDto followDto);

    Result unfollow(FollowDto followDto);

    Result hasFollowed(FollowDto followDto);

    long findFolloweeCount(Long userId, int entityType);

    long findFollowerCount(int entityType, Long entityId);
}
