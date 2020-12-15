package com.liang.entity;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author LiangYonghui
 * @date 2020/11/2 15:14
 * @description
 */
@Data
public class Message {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fromId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long toId;
    private String conversationId;
    private String content;
    private Integer status;
    private Date createTime;
}
