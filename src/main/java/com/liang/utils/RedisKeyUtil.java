package com.liang.utils;

/**
 * @author LiangYonghui
 * @date 2020/10/31 14:08
 * @description
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_ENTITY_COLLECT = "collect:entity";
    private static final String PREFIX_ENTITY_READ = "read:entity";
    private static final String PREFIX_ENTITY_COOMENT = "comment:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_USER_COLLECT = "collect:user";
    private static final String PREFIX_USER_READ = "read:user";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    private static final String PREFIX_USER = "user";
    private static final String PREFIX_USER_COLLECT_ARTICLES = "user:collect:articles";


    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, Long entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }


    // 某个实体的收藏
    // collect:entity:entityType:entityId -> set(userId)
    public static String getEntityCollectKey(int entityType, Long entityId) {
        return PREFIX_ENTITY_COLLECT + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个实体的阅读数
    // read:entity:entityType:entityId -> set(userId)
    public static String getEntityReadKey(int entityType, Long entityId) {
        return PREFIX_ENTITY_READ + SPLIT + entityType + SPLIT + entityId;
    }


    // 某个用户的赞
    // like:user:userId -> int
    public static String getUserLikeKey(Long userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }


    // 某个用户的收藏数
    public static String getUserCollectKey(Long userId) {
        return PREFIX_USER_COLLECT + SPLIT + userId;
    }

    // 某个用户收藏的文章
    public static String getUserArticleCollectKey(Long userId) {
        return PREFIX_USER_COLLECT_ARTICLES + SPLIT + userId;
    }

    // 某个用户的阅读数
    public static String getUserReadKey(Long userId) {
        return PREFIX_USER_READ + SPLIT + userId;
    }

    // 某个用户关注的实体
    // followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(Long userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个实体拥有的粉丝
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType, Long entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }


    // 用户
    public static String getUserKey(Long userId) {
        return PREFIX_USER + SPLIT + userId;
    }


    public static String getEntityCommentKey(int entityType, Long entityId) {
        return PREFIX_ENTITY_COOMENT + SPLIT + entityType + SPLIT + entityId;
    }
}
