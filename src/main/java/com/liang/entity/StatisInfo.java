package com.liang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author LiangYonghui
 * @date 2020/10/13 13:09
 * @description  统计信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StatisInfo {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;


    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private Integer attention;

    private Integer fans;

    private Integer likeCount;

    private Integer words;

    private Integer articleCount;
}
