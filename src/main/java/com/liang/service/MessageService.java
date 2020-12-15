package com.liang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liang.common.lang.Result;
import com.liang.dto.GetMsgDto;
import com.liang.dto.MsgDto;
import com.liang.entity.Message;

import java.util.List;

/**
 * @author LiangYonghui
 * @date 2020/11/2 15:15
 * @description
 */
public interface MessageService extends IService<Message> {
    Result getMessageCount(Long userId);

    Result getLikeAndCollect(Long userId);

    Result read(String messageId);

    Result getCommentAndReply(Long userId);

    Result getFollow(Long userId);

    Result sendMsg(MsgDto msgDto);

    Result getMsg(GetMsgDto getMsgDto);

    Result selectLastMsg(Long userId);
}
