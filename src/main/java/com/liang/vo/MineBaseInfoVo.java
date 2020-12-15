package com.liang.vo;

import lombok.Data;

/**
 * @author LiangYonghui
 * @date 2020/10/12 20:15
 * @description  我的主页中的基本信息
 */
@Data
public class MineBaseInfoVo {
    private Integer attention;
    private Integer fans;
    private Integer like;
    private Integer words;
    private Integer articleCount;

}
