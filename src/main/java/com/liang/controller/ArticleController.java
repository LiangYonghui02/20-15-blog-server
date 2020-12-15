package com.liang.controller;


import com.liang.common.lang.Result;
import com.liang.dto.ArticleDto;
import com.liang.dto.ReadDto;
import com.liang.entity.Article;
import com.liang.service.ArticlePollService;
import com.liang.service.ArticleService;
import com.liang.utils.BlogConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController implements BlogConstant {
    @Autowired
    HttpServletRequest req;

    @Autowired
    ArticleService articleService;

    @Autowired
    private ArticlePollService articlePollService;

    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleDto articleDto) {
        Long userId = getUserId(req);
        articleDto.setUserId(userId);
        String nickname = getNickname(req);
        return articleService.publish(articleDto,nickname);
    }


    @GetMapping("/essay/{id}")
    public Result essay(@PathVariable Long id) {
        return articleService.findEssayById(id);
    }



    @GetMapping("/list_articles")
    public Result listArticles() {
        System.out.println("=========================");
        List<Article> list = articleService.list();
        // 添加文章的阅读数
        for (Article article : list) {
            int readCount = articleService.findEntityReadCount(BlogConstant.ENTITY_TYPE_ARTICLE, article.getId());
            int likeCount = articlePollService.findEntityLikeCount(BlogConstant.ENTITY_TYPE_ARTICLE,article.getId());
            article.setPollCount(likeCount);
            article.setReadCount(readCount);
        }
        System.out.println(list);
        return Result.success(list);
    }


    @PostMapping("/read")
    public Result addRead(@RequestBody ReadDto readDto) {
        Long userId = getUserId(req);
        readDto.setUserId(userId);
        return articleService.addRead(readDto);
    }

    @PostMapping("/is_read")
    public Result isRead(@RequestBody ReadDto readDto) {
        System.out.println("is_read");
        Long userId = getUserId(req);
        readDto.setUserId(userId);
        System.out.println(readDto);
        return articleService.isRead(readDto);
    }

    @GetMapping("get_read_count/{id}")
    public Result getReadCount(@PathVariable String id) {
        return articleService.getReadCount(BlogConstant.ENTITY_TYPE_ARTICLE, Long.parseLong(id));
    }
}
