package com.liang.controller;

import com.liang.annotation.TokenFree;
import com.liang.common.lang.Result;
import com.liang.entity.User;
import com.liang.service.ArticleService;
import com.liang.service.MineService;
import com.liang.service.StatisInfoService;
import com.liang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author LiangYonghui
 * @date 2020/10/12 20:54
 * @description
 */
@RestController
@RequestMapping("/mine")
public class MineController extends BaseController {


    @Autowired
    private MineService mineService;

    @Autowired
    private StatisInfoService statisInfoService;



    // 弃用
    @GetMapping("/mine_article")
    public Result mineArticle() {
        Long userId = getUserId(req);
        String nickname = getNickname(req);
        return mineService.getMineArticle(userId,nickname);
    }

    @GetMapping("/mine_article/{id}")
    public Result getMineArticle(@PathVariable String id) {
        System.out.println(id);
        User user = userService.getById(id);
        String nickname = user.getNickname();
        System.out.println(nickname);
        return mineService.getMineArticle(Long.parseLong(id),nickname);
    }

    @GetMapping("/other_article/{id}")
    public Result otherArticle(@PathVariable Long id) {
        return mineService.mineArticle(id);
    }


    // 获取其他作者的基本信息
    @GetMapping("/base_info/{id}")
    public Result getBaseInfo(@PathVariable Long id) {
        return statisInfoService.getBaseInfo(id);
    }


    // 获取收藏的文章
    @GetMapping("/get_collect_articles/{id}")
    public Result getCollectArticles(@PathVariable Long id) {
        return mineService.getCollectArticles(id);
    }


    // 获取关注的人的数据
    @GetMapping("/get_follow_users/{id}")
    public Result getFollowUsers(@PathVariable("id") Long userId) {
        System.out.println("get_follow_users");
        return mineService.getFollowUsers(userId);
    }


}
