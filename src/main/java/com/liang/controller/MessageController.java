package com.liang.controller;

import com.liang.common.lang.Result;
import com.liang.config.WebSocket;
import com.liang.dto.GetMsgDto;
import com.liang.dto.MsgDto;
import com.liang.entity.Message;
import com.liang.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @author LiangYonghui
 * @date 2020/11/2 15:14
 * @description  /message/count
 */
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private HttpServletRequest rep;

    @Autowired
    private WebSocket webSocket;

    /**
     * 获取消息的数量
     * @return
     */
//    @GetMapping("/count")
//    public Result getMessageCount() {
//        System.out.println("getMessageCount");
//        Long userId = getUserId(req);
//        return messageService.getMessageCount(userId);
//    }

    // 获取评论和回复
    @GetMapping("/comment_and_reply")
    public Result getCommentAndReply() {
        Long userId = getUserId(req);
        return messageService.getCommentAndReply(userId);
    }

    // 获取点赞和收藏
    @GetMapping("/like_and_collect")
    public Result getLikeAndCollect() {
        Long userId = getUserId(req);
        return messageService.getLikeAndCollect(userId);
    }


    // 获取关注的信息
    @GetMapping("/get_follow")
    public Result getFollow() {
        Long userId = getUserId(req);
        return messageService.getFollow(userId);
    }


    // 设置已读
    @PostMapping("/read")
    public Result read(@RequestBody HashMap<String, String> map) {
        String messageId = map.get("messageId");
        System.out.println("messageId-->" + map.get("messageId"));
        return messageService.read(messageId);
    }


    @PostMapping("/send_msg")
    public Result sendMsg(@RequestBody MsgDto msgDto) {
        System.out.println(msgDto);
        return messageService.sendMsg(msgDto);
    }

    @PostMapping("/get_msg")
    public Result getMsg(@RequestBody GetMsgDto getMsgDto) {
        System.out.println(getMsgDto);
        System.out.println("getMsg");
        return messageService.getMsg(getMsgDto);
    }

    // 查询最新的一条信息
    @PostMapping("/select_last_msg")
    public Result selectLastMsg() {
        System.out.println("select_last_msg");
        Long userId = getUserId(req);
        return messageService.selectLastMsg(userId);
    }




}
