package com.liang.service;

import com.liang.common.lang.Result;
import com.liang.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
public interface UserInfoService extends IService<UserInfo> {

    Result getSetting(Long userId);

    Result updateUrl(String url, Long userId);

    Result getUrlById(long userId);
}
