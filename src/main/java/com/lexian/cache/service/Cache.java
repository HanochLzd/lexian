package com.lexian.cache.service;

/**
 * @Author :lwy
 * @Date : 2018/12/10 18:58
 * @Description :
 */
public interface Cache {

    Object getCache(String key);
    Boolean putCache(final String key, Object result, final int expireTime);
}
