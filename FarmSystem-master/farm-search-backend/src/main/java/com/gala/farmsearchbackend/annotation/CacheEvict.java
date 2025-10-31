// CacheEvict.java
package com.gala.farmsearchbackend.annotation;

import java.lang.annotation.*;
//清除缓存注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheEvict {
    /**
     * 缓存key
     */
    String[] key();

    /**
     * 是否删除所有相关key
     */
    boolean allEntries() default false;

    /**
     * 删除key的模式（用于allEntries=true时）
     */
    String pattern() default "";
}