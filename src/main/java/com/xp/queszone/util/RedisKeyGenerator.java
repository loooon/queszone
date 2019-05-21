package com.xp.queszone.util;

public class RedisKeyGenerator {
    private static String SPLIT = ":";
    private static String LIKE = "LIKE";
    private static String DISLIKE = "DISLIKE";
    private static String EVENTQUEUE = "EVENTQUEUE";
    //粉丝
    private static String FOLLOWER = "FOLLOWER";
    //关注对象
    private static String FOLLOWEE = "FOLLOWEE";


    public static String getLikeKey(int entityType, int entityId) {
        return LIKE + SPLIT + String.valueOf(entityType) + String.valueOf(entityId);
    }

    public static String getDISlikeKey(int entityType, int entityId) {
        return DISLIKE + SPLIT + String.valueOf(entityType) + String.valueOf(entityId);
    }

    public static String getEventQueueKey() {
        return EVENTQUEUE;
    }

    //每个实体的粉丝的列表key
    public static String getFollowerKey(int entityType, int entityId) {
        return FOLLOWER + SPLIT + String.valueOf(entityType) + String.valueOf(entityId);
    }

    //用户关注的某类实体列表
    public static String getFolloweeKey(int userId, int entityType) {
        return FOLLOWEE + SPLIT + String.valueOf(userId) + String.valueOf(entityType);
    }
}
