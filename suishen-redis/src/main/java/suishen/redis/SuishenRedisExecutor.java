package suishen.redis;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Author: Bryant Hang
 * Date: 15/5/13
 * Time: 11:30
 */
public class SuishenRedisExecutor<T> {
    static Logger LOGGER = Logger.getLogger(SuishenRedisExecutor.class);

    public T exe(SuishenRedisRunner<T> jedisRunner, SuishenRedis redis) {
        T rst = null;
        Jedis j = null;
        try {
            j = redis.getClient();
            rst = jedisRunner.process(j);
            redis.onFinally(j);
        } catch (JedisConnectionException e) {
            redis.onBrokenFinally(j);
            LOGGER.error(e, e);
        } catch (Exception e) {
            redis.onFinally(j);
            LOGGER.error(e, e);
        }

        return rst;
    }

    public T exe(SuishenRedisRunner<T> jedisRunner, JedisPool jedisPool) {
        T rst = null;
        Jedis j = null;
        try {
            j = jedisPool.getResource();
            rst = jedisRunner.process(j);
            jedisPool.returnResource(j);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(j);
            LOGGER.error(e, e);
        } catch (Exception e) {
            jedisPool.returnResource(j);
            LOGGER.error(e, e);
        }

        return rst;
    }
}
