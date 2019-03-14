package com.lexian.cache.service;

import com.google.common.base.Charsets;
import com.lexian.BaseServerTest;
import com.lexian.utils.KryoUtils;
import org.junit.Test;
import suishen.redis.SuishenRedisExecutor;
import suishen.redis.SuishenRedisTemplate;

import javax.annotation.Resource;

public class RedisServiceTest extends BaseServerTest {

    @Resource
    private SuishenRedisTemplate suishenRedisTemplate;

    @Test
    public void test() {

        String key = "lzd123";
        String result = "1577916xxxx";

        //序列化
        final byte[] data = KryoUtils.serialize(result);

        Boolean cacheResult = new SuishenRedisExecutor<Boolean>().exe(jedis -> {
            String result1 = jedis.set(key.getBytes(Charsets.UTF_8), data);
            Long expire = jedis.expire(key.getBytes(Charsets.UTF_8), 5 * 6 * 60);
            return "ok".equalsIgnoreCase(result1);
        }, suishenRedisTemplate);

        System.out.println("-------\n-\n-\n-\n");
        System.out.println(cacheResult);

    }

    @Test
    public void getDataFromRedis() {
        String key = "lzd123";
        byte[] result = new SuishenRedisExecutor<byte[]>().exe(jedis ->
                jedis.get(key.getBytes(Charsets.UTF_8)), suishenRedisTemplate);

        System.out.println("-------\n-\n-\n-\n");
        System.out.println(result);
        System.out.println(KryoUtils.unSerialize(result));
    }

}
