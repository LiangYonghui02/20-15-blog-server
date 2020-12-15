package com.liang.utils;

/**
 * @author LiangYonghui
 * @date 2020/10/31 14:06
 * @description
 */
public interface BlogConstant {
    /**
     * 实体类型: 帖子
     */
    int ENTITY_TYPE_ARTICLE = 1;

    /**
     * 实体类型: 评论
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * 实体类型: 用户
     */
    int ENTITY_TYPE_USER = 3;

    /**
     * 实体类型：收藏
     */
    int ENTITY_TYPE_COLLECT = 4;

    /**
     * 阅读数
     */
    int ENTITY_TYPE_READ = 5;

    /**
     * 主题: 评论
     */
    String TOPIC_COMMENT = "comment";

    /**
     * 主题: 点赞
     */
    String TOPIC_LIKE = "like";

    /**
     * 主题：收藏
     */
    String TOPIC_COLLECT = "collect";

    /**
     * 主题: 关注
     */
    String TOPIC_FOLLOW = "follow";

    /**
     * 主题: 发帖
     */
    String TOPIC_PUBLISH = "publish";

    /**
     * 主题: 删帖
     */
    String TOPIC_DELETE = "delete";

    /**
     * 主题: 分享
     */
    String TOPIC_SHARE = "share";

    /**
     * 系统用户ID
     */
    int SYSTEM_USER_ID = 1;

    /**
     * 权限: 普通用户
     */
    String AUTHORITY_USER = "user";

    /**
     * 权限: 管理员
     */
    String AUTHORITY_ADMIN = "admin";

    /**
     * 权限: 版主
     */
    String AUTHORITY_MODERATOR = "moderator";

}
