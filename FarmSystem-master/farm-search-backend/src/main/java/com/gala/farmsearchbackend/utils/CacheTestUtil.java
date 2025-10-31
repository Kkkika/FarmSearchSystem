// CacheTestUtil.java
package com.gala.farmsearchbackend.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class CacheTestUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(CacheTestUtil.class);
    /**
     * 检查缓存是否存在
     */
    public boolean isCached(String key) {
        Boolean exists = redisTemplate.hasKey("cache:" + key);
        return exists != null && exists;
    }

    /**
     * 获取缓存值
     */
    public Object getCachedValue(String key) {
        return redisTemplate.opsForValue().get("cache:" + key);
    }

    /**
     * 获取缓存剩余时间
     */
    public Long getCacheTtl(String key) {
        return redisTemplate.getExpire("cache:" + key, TimeUnit.SECONDS);
    }

    /**
     * 清理所有测试缓存
     */
    public void clearTestCaches() {
        // 清理测试相关的缓存
        redisTemplate.delete(redisTemplate.keys("cache:test:*"));
        redisTemplate.delete(redisTemplate.keys("cache:province:*"));
        redisTemplate.delete(redisTemplate.keys("cache:city:*"));
        redisTemplate.delete(redisTemplate.keys("cache:node:*"));
        redisTemplate.delete(redisTemplate.keys("cache:farm:*"));
        redisTemplate.delete(redisTemplate.keys("cache:slau:*"));
        redisTemplate.delete(redisTemplate.keys("cache:whol:*"));
        redisTemplate.delete(redisTemplate.keys("cache:reta:*"));
        log.info("测试缓存清理完成");
    }

    /**
     * 打印缓存信息（用于调试）
     */
    public void printCacheInfo(String key) {
        boolean exists = isCached(key);
        Object value = getCachedValue(key);
        Long ttl = getCacheTtl(key);

        log.info("缓存信息 - Key: {}, 存在: {}, TTL: {}秒, 值: {}",
                key, exists, ttl, value);
    }
}