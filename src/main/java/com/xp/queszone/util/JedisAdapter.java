package com.xp.queszone.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;

@Service
public class JedisAdapter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool jedisPool;

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("xxx.xxx.xxx.xxx",xxx);
    }

    public Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        jedis.auth("xxxx");
        return jedis;
    }

    public Transaction multi(Jedis jedis) {
        try {
            return jedis.multi();
        } catch (Exception e) {
            logger.error("开启Redis事务异常" + e.getMessage());
        }
        return null;
    }

    public List<Object> exec(Transaction tx, Jedis jedis) {
        try {
            return tx.exec();
        } catch (Exception e) {
            logger.error("Redis事务执行异常" + e.getMessage());
        } finally {
            if (null != tx) {
                try {
                    tx.close();
                } catch (Exception e) {
                    logger.error("Redis事务关闭异常" + e.getMessage());
                }
            }
            if (null != jedis) {
                jedis.close();
            }
        }
        return null;
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.auth("xxxx");
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("sadd发生异常" + e.getMessage());
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
            jedis.auth("xxxx");
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("srem发生异常" + e.getMessage());
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
            jedis.auth("xxxx");
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("scard发生异常" + e.getMessage());
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
            jedis.auth("xxxx");
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("sismember发生异常" + e.getMessage());
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return false;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.auth("xxxx");
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("lpush发生异常" + e.getMessage());
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return 0;
    }

    public List<String> lrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.auth("xxxx");
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("lrange发生异常" + e.getMessage());
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return null;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.auth("xxxx");
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("brpop发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long zadd(String key, double score, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.auth("xxxx");
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
            logger.error("zadd发生异常" + e.getMessage());
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return 0;
    }

    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.auth("xxxx");
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("zrevrange发生异常" + e.getMessage());
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return null;
    }

    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.auth("xxxx");
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("zcard发生异常" + e.getMessage());
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.auth("xxxx");
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error("zscore发生异常" + e.getMessage());
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return null;
    }

}

