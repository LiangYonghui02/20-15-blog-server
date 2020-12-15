package com.liang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    private Date time;

    private String content;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long articleId;

    private Long fatherComment;

    private Integer pollCount;

    private String nickname;

    private String toNickname;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long toUserId;


}
