package com.liang.vo;

import com.liang.entity.Message;
import lombok.Data;


import java.util.List;

/**
 * @author LiangYonghui
 * @date 2020/11/9 20:06
 * @description
 */
@Data
public class GetMsgVo {
    private List<Message> messages;
    private String userUrl; // 自己的url
    private String oppositeUrl; // 对方的url
}
