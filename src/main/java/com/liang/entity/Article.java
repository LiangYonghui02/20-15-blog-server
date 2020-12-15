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
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    private String title;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private Date createTime;

    private Date updateTime;

    /**
     * 概要
     */
    private String summary;

    /**
     * 点赞数
     */
    private Integer pollCount;

    private Integer commentCount;

    private Integer readCount;

    /**
     * 是否公开
     */
    private Integer isOpen;

    private Integer isDel;

    @JsonSerialize(using = ToStringSerializer.class)
    /**
     * 暂不连接，分类id
     */
    private Long classId;

    @JsonSerialize(using = ToStringSerializer.class)
    /**
     * 暂不连接，标签id
     */
    private Long tagId;

    private String writer;


}
