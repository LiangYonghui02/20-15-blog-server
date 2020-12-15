package com.liang.service;

import com.liang.common.lang.Result;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author LiangYonghui
 * @date 2020/10/12 20:58
 * @description
 */
@Mapper
@Repository
public interface MineService {
    Result getMineArticle(Long userId,String nickname);

    Result mineArticle(Long userId);

    Result getCollectArticles(Long id);

    Result getFollowUsers(Long userId);

}
