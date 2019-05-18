package com.xp.queszone.service;

import com.xp.queszone.util.JedisAdapter;
import com.xp.queszone.util.RedisKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    public long getLikeCount(int entityType, int entityId) {
        return jedisAdapter.scard(RedisKeyGenerator.getLikeKey(entityType, entityId));
    }

    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyGenerator.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId))){
            return 1;
        }
        String dislikeKey = RedisKeyGenerator.getDISlikeKey(entityType, entityId);
        if (jedisAdapter.sismember(dislikeKey, String.valueOf(userId))){
            return -1;
        }
        return 0;
    }

    public long like(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyGenerator.getLikeKey(entityType, entityId);
        String dislikeKey = RedisKeyGenerator.getDISlikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));
        jedisAdapter.srem(dislikeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    public long dislike(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyGenerator.getLikeKey(entityType, entityId);
        String dislikeKey = RedisKeyGenerator.getDISlikeKey(entityType, entityId);
        jedisAdapter.sadd(dislikeKey, String.valueOf(userId));
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
}
