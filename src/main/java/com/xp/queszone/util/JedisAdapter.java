package com.xp.queszone.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class JedisAdapter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool jedisPool;

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("***主机ip***",6379);
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.auth("***服务密码***");
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常"+e.getMessage());
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.auth("***服务密码***");
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常"+e.getMessage());
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.auth("***服务密码***");
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常"+e.getMessage());
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.auth("***服务密码***");
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常"+e.getMessage());
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return false;
    }
}
