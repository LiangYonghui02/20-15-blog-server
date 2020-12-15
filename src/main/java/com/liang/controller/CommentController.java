package com.liang.controller;


import com.liang.common.lang.Result;
import com.liang.dto.AddCommentDto;
import com.liang.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Autowired
    HttpServletRequest req;

    @Autowired
    CommentService commentService;


    @PostMapping("/add")
    public Result addComment(@RequestBody AddCommentDto addCommentDto) {
        Long userId = getUserId(req);
        String nickname = getNickname(req);
        addCommentDto.setObserverName(nickname);
        addCommentDto.setUserId(userId);
        return commentService.addComment(addCommentDto);
    }


    @GetMapping("/get_comments/{id}")
    public Result getArticleComments(@PathVariable("id") Long articleId) {
        return commentService.getArticleComments(articleId);
    }

}
