package com.liang.controller;

import com.liang.common.lang.Result;
import com.liang.dto.FollowDto;
import com.liang.service.FollowService;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LiangYonghui
 * @date 2020/11/8 9:22
 * @description
 */
@RestController
@RequestMapping("/follow")
public class FollowController extends BaseController {

    @Autowired
    private HttpServletRequest req;

    @Autowired
    private FollowService followService;

    @PostMapping("/follow")
    public Result follow(@RequestBody FollowDto followDto) {
        System.out.println(followDto);
        Long userId = getUserId(req);
        followDto.setUserId(userId);
        return followService.follow(followDto);
    }

    @PostMapping("/unfollow")
    public Result unfollow(@RequestBody FollowDto followDto) {
        System.out.println(followDto);
        Long userId = getUserId(req);
        followDto.setUserId(userId);
        return followService.unfollow(followDto);
    }

    @PostMapping("/has_follow")
    public Result hasFollowed(@RequestBody FollowDto followDto) {
        System.out.println("has_follow");
        Long userId = getUserId(req);
        followDto.setUserId(userId);
        System.out.println(followDto);
        return followService.hasFollowed(followDto);
    }

}
