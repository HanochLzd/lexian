package com.lexian.cache.annotation;

import java.lang.annotation.*;

/**
 * @Author :lwy
 * @Date : 2018/12/10 11:37
 * @Description :
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable {

    /**
     * 过期时间,默认60秒
     */
    int expireTime() default 3*60;

    /**
     * 设置缓存类型(分布式，本地)
     */
}
