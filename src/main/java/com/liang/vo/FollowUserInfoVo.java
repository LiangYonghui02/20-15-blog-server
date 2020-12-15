package com.liang.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.liang.entity.Article;
import lombok.Data;

import java.sql.Blob;

/**
 * @author LiangYonghui
 * @date 2020/11/11 20:02
 * @description
 */
@Data
public class FollowUserInfoVo {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private String nickname;

    private String email;

    /**
     * 头像url
     */
    private String avatarUrl;

    /**
     * 签名
     */
    private String signature;

    /**
     * 简介
     */
    private String intro;

    private String tel;

    private Integer articleCount;

    // 最新的文章的名字
    private String lastArticleName;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long articleId;



}
