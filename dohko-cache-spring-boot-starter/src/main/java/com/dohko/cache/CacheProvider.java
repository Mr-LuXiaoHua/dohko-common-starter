package com.dohko.cache;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author
 * @desc
 */
public class CacheProvider {

    private RedisTemplate redisTemplate;

    public CacheProvider(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate getRedisTemplate() {
        return this.redisTemplate;
    }
}
