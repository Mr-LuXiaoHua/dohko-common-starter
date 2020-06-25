package com.dohko.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author
 * @desc
 */
@Configuration
@ConditionalOnClass(CacheProvider.class)
@Slf4j
public class CacheAutoConfiguration {

    @Resource
    private RedisTemplate redisTemplate;


    @Bean
    public CacheProvider provider() {
        CacheProvider cacheProvider = new CacheProvider(redisTemplate);
        return cacheProvider;
    }



}
