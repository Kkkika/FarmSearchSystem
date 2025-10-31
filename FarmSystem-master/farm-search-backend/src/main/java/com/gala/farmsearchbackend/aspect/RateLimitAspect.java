// RateLimitAspect.java
package com.gala.farmsearchbackend.aspect;

import com.gala.farmsearchbackend.annotation.RateLimit;
import com.gala.farmsearchbackend.exception.BusinessException;
import com.gala.farmsearchbackend.exception.ErrorCode;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
//限流切面类
@Aspect
@Component
public class RateLimitAspect {
    private static final Logger log = LoggerFactory.getLogger(RateLimitAspect.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Before("@annotation(rateLimit)")
    public void doBefore(JoinPoint point, RateLimit rateLimit) {
        String key = generateKey(point, rateLimit);
        int time = rateLimit.time();
        int count = rateLimit.count();
        TimeUnit timeUnit = rateLimit.timeUnit();

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        String redisKey = "rate_limit:" + key;

        Integer currentCount = (Integer) ops.get(redisKey);
        if (currentCount == null) {
            // 第一次访问
            ops.set(redisKey, 1, time, timeUnit);
        } else if (currentCount < count) {
            // 计数加1
            ops.set(redisKey, currentCount + 1, getExpire(redisKey), TimeUnit.SECONDS);
        } else {
            log.warn("接口限流：key={}, 限制次数={}, 当前次数={}", key, count, currentCount);
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST, "请求过于频繁，请稍后再试");
        }
    }

    private String generateKey(JoinPoint point, RateLimit rateLimit) {
        StringBuilder key = new StringBuilder(rateLimit.key());
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        key.append(method.getDeclaringClass().getName())
                .append(".")
                .append(method.getName());

        // 根据限流类型添加不同的标识
        switch (rateLimit.limitType()) {
            case IP:
                key.append(":").append(getClientIP());
                break;
            case USER:
                key.append(":").append(getCurrentUserId());
                break;
            default:
                break;
        }

        return key.toString();
    }

    private String getClientIP() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private String getCurrentUserId() {
        // 从请求属性中获取用户ID（在JWT拦截器中设置）
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Object userId = request.getAttribute("userId");
        return userId != null ? userId.toString() : "anonymous";
    }

    private Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
}