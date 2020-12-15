package com.liang.service;

import com.liang.common.lang.Result;
import com.liang.dto.CollectDto;

/**
 * @author LiangYonghui
 * @date 2020/10/31 14:31
 * @description
 */

public interface CollectService {
    Result collect(CollectDto collectDto);

    Integer findEntityCollectCount(Integer entityType, Long entityId);

    Integer findEntityCollectStatus(Long userId, Integer entityType, Long entityId);

    Result isCollect(CollectDto collectDto);

    Result getCollectCount(int entityTypeArticle, long parseLong);
}
