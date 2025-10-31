// CacheAspect.java
package com.gala.farmsearchbackend.aspect;

import com.gala.farmsearchbackend.annotation.Cacheable;
import com.gala.farmsearchbackend.annotation.CacheEvict;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;
//缓存切面类
@Aspect
@Component
public class CacheAspect {
    private static final Logger log = LoggerFactory.getLogger(CacheAspect.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ObjectMapper objectMapper;

    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(cacheable)")
    public Object aroundCacheable(ProceedingJoinPoint point, Cacheable cacheable) throws Throwable {
        String key = parseKey(point, cacheable.key());
        String redisKey = "cache:" + key;

        // 从缓存中获取
        Object cachedValue = redisTemplate.opsForValue().get(redisKey);
        if (cachedValue != null) {
            log.debug("缓存命中：key={}", key);
            return convertToReturnType(cachedValue, point);
        }

        // 执行方法
        Object result = point.proceed();

        // 缓存结果
        if (result != null || cacheable.cacheNull()) {
            redisTemplate.opsForValue().set(
                    redisKey,
                    result,
                    cacheable.expire(),
                    cacheable.timeUnit()
            );
            log.debug("缓存设置：key={}, expire={}{}", key, cacheable.expire(), cacheable.timeUnit());
        }

        return result;
    }

    @Around("@annotation(cacheEvict)")
    public Object aroundCacheEvict(ProceedingJoinPoint point, CacheEvict cacheEvict) throws Throwable {
        // 先执行方法
        Object result = point.proceed();

        // 删除缓存
        if (cacheEvict.allEntries()) {
            String pattern = "cache:" + cacheEvict.pattern() + "*";
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.debug("批量删除缓存：pattern={}, count={}", pattern, keys.size());
            }
        } else {
            String key = parseKey(point, Arrays.toString(cacheEvict.key()));
            String redisKey = "cache:" + key;
            redisTemplate.delete(redisKey);
            log.debug("删除缓存：key={}", key);
        }

        return result;
    }

    private String parseKey(ProceedingJoinPoint point, String keyExpression) {
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            Object[] args = point.getArgs();
            String[] parameterNames = signature.getParameterNames();

            StandardEvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }

            Expression expression = parser.parseExpression(keyExpression);
            return expression.getValue(context, String.class);
        } catch (Exception e) {
            log.warn("解析缓存key表达式失败：{}", keyExpression, e);
            return keyExpression;
        }
    }

    private Object convertToReturnType(Object cachedValue, ProceedingJoinPoint point) {
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Class<?> returnType = signature.getReturnType();

            if (cachedValue.getClass().equals(returnType)) {
                return cachedValue;
            }

            // 如果是复杂类型，使用JSON转换
            return objectMapper.convertValue(cachedValue, returnType);
        } catch (Exception e) {
            log.warn("缓存值类型转换失败", e);
            return cachedValue;
        }
    }
}