package com.lexian.cache.service;

import com.google.common.base.Charsets;
import com.lexian.utils.KryoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import suishen.redis.SuishenRedisExecutor;
import suishen.redis.SuishenRedisTemplate;

/**
 * @Author :lwy
 * @Date : 2018/12/10 15:17
 * @Description :
 */
@Service
public class RedisCacheService implements Cache {

    private static final Logger LOG = LoggerFactory.getLogger(RedisCacheService.class);

    private final int EXPIRE_TIME = 60;

    @Autowired
    private SuishenRedisTemplate suishenRedisTemplate;

    /**
     * 根据key查询
     *
     * @param key
     * @return
     */
    @Override
    public Object getCache(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        //byte[] results = suishenRedisTemplate.get(key.getBytes(Charsets.UTF_8));

        byte[] results = new SuishenRedisExecutor<byte[]>()
                .exe(jedis -> jedis.get(key.getBytes(Charsets.UTF_8)), suishenRedisTemplate);

        if (results == null) {
            return null;
        }

        //命中缓存--延长过期时间
        Boolean cacheResult = new SuishenRedisExecutor<Boolean>().exe(jedis -> {
            Long expire = jedis.expire(key.getBytes(Charsets.UTF_8), EXPIRE_TIME);
            return 1 == expire;
        }, suishenRedisTemplate);

        if (!cacheResult) {
            LOG.warn("Cache expiration time update failed");
        }
        return KryoUtils.unSerialize(results);
    }


    /**
     * 设置缓存
     *
     * @param key
     * @param result
     * @param expireTime
     */
    @Override
    public Boolean putCache(final String key, Object result, final int expireTime) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        //序列化
        final byte[] data = KryoUtils.serialize(result);

        Boolean cacheResult = new SuishenRedisExecutor<Boolean>().exe(jedis -> {
            String result1 = jedis.set(key.getBytes(Charsets.UTF_8), data);
            Long expire = jedis.expire(key.getBytes(Charsets.UTF_8), expireTime);
            return "ok".equalsIgnoreCase(result1);
        }, suishenRedisTemplate);

        return cacheResult;
    }
}
