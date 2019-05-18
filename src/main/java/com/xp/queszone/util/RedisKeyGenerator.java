package com.xp.queszone.util;

public class RedisKeyGenerator {
    private static String SPLIT=":";
    private static String LIKE="LIKE";
    private static String DISLIKE="DISLIKE";

    public static String getLikeKey(int entityType, int entityId) {
        return LIKE + SPLIT + String.valueOf(entityType) + String.valueOf(entityId);
    }

    public static String getDISlikeKey(int entityType, int entityId) {
        return DISLIKE + SPLIT + String.valueOf(entityType) + String.valueOf(entityId);
    }
}
