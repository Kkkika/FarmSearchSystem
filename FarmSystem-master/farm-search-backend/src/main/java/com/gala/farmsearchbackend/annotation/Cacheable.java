// Cacheable.java
package com.gala.farmsearchbackend.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;
//缓存注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable {
    /**
     * 缓存key
     */
    String key();

    /**
     * 过期时间
     */
    long expire() default 300;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 是否缓存空值
     */
    boolean cacheNull() default false;
}