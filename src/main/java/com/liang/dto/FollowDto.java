package com.liang.dto;

import lombok.Data;

/**
 * @author LiangYonghui
 * @date 2020/11/8 9:43
 * @description
 */
@Data
public class FollowDto {
    Long userId; // 触发关注的人的id
    Long entityId;  // 被关注的人的id
    Integer entityType;
}
