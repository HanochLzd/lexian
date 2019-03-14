package suishen.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Author: Bryant Hang
 * Date: 15/5/13
 * Time: 11:31
 */
public interface SuishenRedisRunner<T> {
    T process(Jedis jedis) throws JedisConnectionException;
}
