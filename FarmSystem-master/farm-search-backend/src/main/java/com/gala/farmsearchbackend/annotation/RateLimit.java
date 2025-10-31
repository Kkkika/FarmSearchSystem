// RateLimit.java
package com.gala.farmsearchbackend.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;
//限流注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    /**
     * 限流key
     */
    String key() default "rate_limit:";

    /**
     * 时间窗口，单位秒
     */
    int time() default 60;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 允许请求次数
     */
    int count() default 10;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;

    enum LimitType {
        DEFAULT,    // 默认，根据方法名限流
        IP,         // 根据IP限流
        USER        // 根据用户ID限流
    }
}