package com.liang.controller;


import com.liang.common.lang.Result;
import com.liang.dto.ArticlePollDto;
import com.liang.service.ArticlePollService;
import com.liang.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
@RestController
@RequestMapping("/article_poll")
public class ArticlePollController extends BaseController{

    @Autowired
    private ArticlePollService articlePollService;


    @Autowired
    private HttpServletRequest req;

//    @GetMapping("/poll")
//    public Result poll(@RequestParam Long articleId) {
//        Long userId = getUserId(req);
//        return articlePollService.poll(userId,articleId);
//    }

    @PostMapping("/poll")
    public Result poll(@RequestBody ArticlePollDto articlePollDto) {
        Long userId = getUserId(req);
        articlePollDto.setUserId(userId);
        return articlePollService.poll(articlePollDto);
    }

    @GetMapping("/unpoll")
    public Result unPoll(@RequestParam Long articleId) {
        Long userId = getUserId(req);
        return articlePollService.unPoll(userId,articleId);
    }

    // 查看当前用户是否对该文章进行了点赞
//    @GetMapping("/has_poll")
//    public Result hasPoll(@RequestParam Long articleId) {
//        Long userId = getUserId(req);
//        return articlePollService.hasPoll(userId,articleId);
//    }


    // 查看当前用户是否对该文章进行了点赞
    @PostMapping("/has_poll")
    public Result hasPoll(@RequestBody ArticlePollDto articlePollDto) {
        Long userId = getUserId(req);
        articlePollDto.setUserId(userId);
        int likeStatus = articlePollService.
                findEntityLikeStatus(articlePollDto.getUserId(),
                        articlePollDto.getEntityType(), articlePollDto.getEntityId());
        Map<String,Object> map = new HashMap();
        map.put("likeStatus",likeStatus);
        return Result.success(map);
    }



    @PostMapping("/get_article_poll")
    public Result getPollCunt(@RequestBody ArticlePollDto articlePollDto) {
        System.out.println("getArticlePollCunt");
        System.out.println(articlePollDto);
        int likeCount = articlePollService.findEntityLikeCount(articlePollDto.getEntityType(), articlePollDto.getEntityId());
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount",likeCount);
        return Result.success(map);
    }


}
