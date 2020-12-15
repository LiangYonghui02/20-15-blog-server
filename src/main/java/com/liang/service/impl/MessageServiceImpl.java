package com.liang.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liang.common.lang.Result;
import com.liang.config.WebSocket;
import com.liang.dto.GetMsgDto;
import com.liang.dto.MsgDto;
import com.liang.entity.*;
import com.liang.mapper.MessageMapper;
import com.liang.mapper.UserInfoMapper;
import com.liang.service.MessageService;
import com.liang.utils.BlogConstant;
import com.liang.vo.GetMsgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author LiangYonghui
 * @date 2020/11/2 15:16
 * @description
 */
@Service
@Transactional
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService, BlogConstant {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Result getMessageCount(Long userId) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("to_id",userId).eq("status",0);
        Integer count = messageMapper.selectCount(wrapper);
        System.out.println("count----->"+count);
        Map<String,Integer> map = new HashMap<>();
        map.put("messageCount",count);
        return Result.success(map);
    }

    // 最后要改写成不要自己的
    @Override
    public Result getLikeAndCollect(Long userId) {
        QueryWrapper<Message> messageWrapper = new QueryWrapper<>();
        messageWrapper.eq("to_id",userId) .eq("status",0);
        messageWrapper.and(wrapper -> wrapper .eq("conversation_id",BlogConstant.TOPIC_COLLECT)
                .or().eq("conversation_id", BlogConstant.TOPIC_LIKE)).orderByDesc("create_time");
        List<Message> messages = messageMapper.selectList(messageWrapper);
        System.out.println(messages);
        System.out.println(messages.size());
        return Result.success(messages);
    }

    @Override
    public Result read(String messageId) {
        Message message = new Message();
        message.setId(Long.parseLong(messageId));
        message.setStatus(1);
        messageMapper.updateById(message);
        return Result.success();
    }

    @Override
    public Result getCommentAndReply(Long userId) {
        System.out.println("----------getCommentAndReply--------------");
        QueryWrapper<Message> messageWrapper = new QueryWrapper<>();
        messageWrapper.eq("to_id",userId) .eq("status",0)
                .eq("conversation_id", BlogConstant.TOPIC_COMMENT)
                .orderByDesc("create_time");
        List<Message> messages = messageMapper.selectList(messageWrapper);
        System.out.println(messages);
        System.out.println(messages.size());
        return Result.success(messages);
    }

    @Override
    public Result getFollow(Long userId) {
        System.out.println("----------getFollow--------------");
        System.out.println(userId);
        QueryWrapper<Message> messageWrapper = new QueryWrapper<>();
        messageWrapper.eq("to_id",userId) .eq("status",0)
                .eq("conversation_id", BlogConstant.TOPIC_FOLLOW)
                .orderByDesc("create_time");
        List<Message> messages = messageMapper.selectList(messageWrapper);
        System.out.println(messages);
        System.out.println(messages.size());
        return Result.success(messages);
    }

    @Override
    public Result sendMsg(MsgDto msgDto) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.setFromId(msgDto.getFromId());
                message.setToId(msgDto.getToId());
                message.setStatus(0);
                message.setConversationId(msgDto.getConversationId());
                message.setCreateTime(new Date());
                message.setContent(msgDto.getContent());
                System.out.println( "---------insert------------" +message + "----------insert-----------");
                int insert = messageMapper.insert(message);
                System.out.println("insert");
                System.out.println(insert);
                System.out.println("insert");

            }
        };
        executorService.submit(runnable);
        webSocket.sendOneMessage(msgDto.getConversationId(),msgDto.getContent());
        return Result.success();
    }

    @Override
    public Result getMsg(GetMsgDto getMsgDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(getMsgDto.getUserId()).append(getMsgDto.getOppositeId());
        System.out.println(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getMsgDto.getOppositeId()).append(getMsgDto.getUserId());
        System.out.println(sb2.toString());

        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("conversation_id",sb.toString()).or().eq("conversation_id",sb2.toString());
        List<Message> messages = messageMapper.selectList(wrapper);
        GetMsgVo getMsgVo = new GetMsgVo();
        getMsgVo.setMessages(messages);

        QueryWrapper<UserInfo> myInfoWrapper = new QueryWrapper<>();
        myInfoWrapper.eq("user_id",getMsgDto.getUserId());
        UserInfo myInfo = userInfoMapper.selectOne(myInfoWrapper);

        QueryWrapper<UserInfo> oppositeWrapper = new QueryWrapper<>();
        oppositeWrapper.eq("user_id",getMsgDto.getOppositeId());
        UserInfo oppositeInfo = userInfoMapper.selectOne(oppositeWrapper);

        getMsgVo.setUserUrl(myInfo.getAvatarUrl());
        getMsgVo.setOppositeUrl(oppositeInfo.getAvatarUrl());

        System.out.println(getMsgVo);

        System.out.println(getMsgVo);
        return Result.success(getMsgVo);
    }

    @Override
    public Result selectLastMsg(Long userId) {
//        QueryWrapper<Message> wrapper = new QueryWrapper<>();

//        wrapper.eq("to_id", userId).;
//        List<Message> messages = messageMapper.selectLastMsg(userId);
//
//        System.out.println("查询用户每个对话的最新消息");
//
//        System.out.println(messages);

//        return Result.success(messages);
        List<Message> messages = new ArrayList<>();

        List<String> lastMsgDate = messageMapper.selectLastMsgDate(userId);
        for (String s : lastMsgDate) {
            QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("create_time",s);
            Message message = messageMapper.selectOne(queryWrapper);
            messages.add(message);
        }


        System.out.println("======================"+messages+"=============================");



        return Result.success(messages);
    }
}
