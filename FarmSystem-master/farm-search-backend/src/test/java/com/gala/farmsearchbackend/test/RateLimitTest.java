// RateLimitTest.java
package com.gala.farmsearchbackend.test;

import com.gala.farmsearchbackend.utils.CacheTestUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RateLimitTest {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private CacheTestUtil cacheTestUtil;
    private static final Logger log = LoggerFactory.getLogger(RateLimitTest.class);
    @Test
    void testRateLimitKeyGeneration() {
        log.info("=== 测试限流Key生成 ===");

        // 模拟限流key
        String rateLimitKey = "rate_limit:admin_login:127.0.0.1";
        redisTemplate.opsForValue().set(rateLimitKey, 1, 60, java.util.concurrent.TimeUnit.SECONDS);

        Boolean exists = redisTemplate.hasKey(rateLimitKey);
        assertTrue(exists != null && exists, "限流Key应该存在");

        Integer count = (Integer) redisTemplate.opsForValue().get(rateLimitKey);
        assertEquals(1, count, "限流计数应该为1");

        Long ttl = redisTemplate.getExpire(rateLimitKey, java.util.concurrent.TimeUnit.SECONDS);
        assertTrue(ttl != null && ttl > 0, "限流Key应该有TTL");

        log.info("限流Key测试通过 - Key: {}, Count: {}, TTL: {}秒", rateLimitKey, count, ttl);
    }

    @Test
    void testRateLimitExpiration() throws InterruptedException {
        log.info("=== 测试限流过期 ===");

        String testKey = "rate_limit:test:" + System.currentTimeMillis();
        redisTemplate.opsForValue().set(testKey, 5, 2, java.util.concurrent.TimeUnit.SECONDS);

        // 立即检查
        assertTrue(redisTemplate.hasKey(testKey), "限流Key应该存在");

        // 等待过期
        Thread.sleep(3000);

        // 再次检查
        assertFalse(redisTemplate.hasKey(testKey), "限流Key应该已过期");

        log.info("限流过期测试通过");
    }
}