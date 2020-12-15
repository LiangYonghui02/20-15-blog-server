package com.liang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liang.common.lang.Result;
import com.liang.entity.StatisInfo;


/**
 * @author LiangYonghui
 * @date 2020/10/13 13:12
 * @description
 */
public interface StatisInfoService extends IService<StatisInfo> {
    Result getBaseInfo(Long id);
    public int findUserLikeCount(long userId);


}
