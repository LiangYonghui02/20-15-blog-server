package com.liang.controller;


import com.liang.common.lang.Result;
import com.liang.dto.CommentPollDto;
import com.liang.service.CommentPollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/comment_poll")
public class CommentPollController extends BaseController {
    @Autowired
    HttpServletRequest req;

    @Autowired
    private CommentPollService commentPollService;


    @RequestMapping("/add")
    public Result addCommentPoll(@RequestBody CommentPollDto commentPollDto) {
        System.out.println("comment_poll");
        System.out.println(commentPollDto);
        Long userId = getUserId(req);
        commentPollDto.setUserId(userId);
        return commentPollService.addCommentPoll(commentPollDto);
    }



    @PostMapping("is_comment_poll")
    public Result isCommentPoll(@RequestBody CommentPollDto commentPollDto) {
        Long userId = getUserId(req);
        commentPollDto.setUserId(userId);
        int likeStatus = commentPollService.
                findEntityLikeStatus(commentPollDto.getUserId(),
                        commentPollDto.getEntityType(), commentPollDto.getEntityId());
        Map<String,Object> map = new HashMap();
        map.put("likeStatus",likeStatus);
        return Result.success(map);
    }

    @PostMapping("/get_comment_poll")
    public Result getCommentPoll(@RequestBody CommentPollDto commentPollDto) {
        System.out.println("get_comment_poll");
        System.out.println(commentPollDto);
        int likeCount = commentPollService.findEntityLikeCount(commentPollDto.getEntityType(), commentPollDto.getEntityId());
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount",likeCount);
        return Result.success(map);
    }

}
