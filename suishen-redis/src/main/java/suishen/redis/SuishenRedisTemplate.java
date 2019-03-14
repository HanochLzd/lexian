package suishen.redis;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisConnectionException;
import suishen.redis.SuishenRedis;
import suishen.redis.exception.RedisRuntimeException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis operation template support master/slave
 * <p/>
 * Author: Bryant Hang
 * Date: 3/30/15
 * Time: 11:56 AM
 */
public class SuishenRedisTemplate extends SuishenRedis {

    private SuishenRedis slaveRedisIns = null;

    public SuishenRedisTemplate(String url, String slaveUrl, JedisPoolConfig jedisPoolConfig, int timeout) {
        super(url, jedisPoolConfig, timeout);

        if (!super.isAlive()) {
            throw new RedisRuntimeException("fialed to get redis client " + url);
        }

        if (StringUtils.isNotBlank(slaveUrl)) {
            slaveRedisIns = new SuishenRedis(slaveUrl, jedisPoolConfig, timeout);
            if (!slaveRedisIns.isAlive()) {
                throw new RedisRuntimeException("fialed to get slave redis client " + slaveUrl);
            }
        } else {
            slaveRedisIns = new SuishenRedis(url, jedisPoolConfig, timeout);
        }
    }

    @Override
    public String get(String key) {
        return slaveRedisIns.get(key);
    }

    @Override
    public <T extends Serializable> T get(byte[] key) {
        return slaveRedisIns.get(key);
    }

    @Override
    public Boolean exists(String key) {
        return slaveRedisIns.exists(key);
    }

    @Override
    public String type(String key) {
        return slaveRedisIns.type(key);
    }

    @Override
    public Long ttl(String key) {
        return slaveRedisIns.ttl(key);
    }

    @Override
    public Boolean getbit(String key, long offset) {
        return slaveRedisIns.getbit(key, offset);
    }

    @Override
    public String getrange(String key, long startOffset, long endOffset) {
        return slaveRedisIns.getrange(key, startOffset, endOffset);
    }

    @Override
    public String substr(String key, int start, int end) {
        return slaveRedisIns.substr(key, start, end);
    }

    @Override
    public String hget(String key, String field) {
        return slaveRedisIns.hget(key, field);
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        return slaveRedisIns.hmget(key, fields);
    }

    @Override
    public Boolean hexists(String key, String field) {
        return slaveRedisIns.hexists(key, field);
    }


    @Override
    public Long hlen(String key) {
        return slaveRedisIns.hlen(key);
    }

    @Override
    public Set<String> hkeys(String key) {
        return slaveRedisIns.hkeys(key);
    }

    @Override
    public List<String> hvals(String key) {
        return slaveRedisIns.hvals(key);
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        return slaveRedisIns.hgetAll(key);
    }


    @Override
    public Long llen(String key) {
        return slaveRedisIns.llen(key);
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        return slaveRedisIns.lrange(key, start, end);
    }

    @Override
    public String lindex(String key, long index) {
        return slaveRedisIns.lindex(key, index);
    }


    @Override
    public Set<String> smembers(String key) {
        return slaveRedisIns.smembers(key);
    }

    @Override
    public Long scard(String key) {
        return slaveRedisIns.scard(key);
    }

    @Override
    public Boolean sismember(String key, String member) {
        return slaveRedisIns.sismember(key, member);
    }

    @Override
    public <T extends Serializable> boolean sismember(byte[] key, T object) {
        return slaveRedisIns.sismember(key, object);
    }

    @Override
    public String srandmember(String key) {
        return slaveRedisIns.srandmember(key);
    }

    @Override
    public <T extends Serializable> T srandmember(byte[] key) {
        return slaveRedisIns.srandmember(key);
    }


    public Set<String> zrange(String key, int start, int end) {
        return slaveRedisIns.zrange(key, start, end);
    }


    @Override
    public Long zrank(String key, String member) {
        return slaveRedisIns.zrank(key, member);
    }

    @Override
    public Long zrevrank(String key, String member) {
        return slaveRedisIns.zrevrank(key, member);
    }

    public Set<String> zrevrange(String key, int start, int end) {

        return slaveRedisIns.zrevrange(key, start, end);

    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, int start, int end) {
        return slaveRedisIns.zrangeWithScores(key, start, end);
    }

    public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
        return slaveRedisIns.zrevrangeWithScores(key, start, end);
    }

    @Override
    public Long zcard(String key) {
        return slaveRedisIns.zcard(key);

    }

    @Override
    public Double zscore(String key, String member) {
        return slaveRedisIns.zscore(key, member);
    }


    @Override
    public Long zcount(String key, double min, double max) {
        return slaveRedisIns.zcount(key, min, max);
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        return slaveRedisIns.zrangeByScore(key, min, max);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        return slaveRedisIns.zrevrangeByScore(key, max, min);
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        return slaveRedisIns.zrangeByScore(key, min, max, offset, count);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        return slaveRedisIns.zrevrangeByScore(key, max, min, offset, count);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        return slaveRedisIns.zrangeByScoreWithScores(key, min, max);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        return slaveRedisIns.zrevrangeByScoreWithScores(key, max, min);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        return slaveRedisIns.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        return slaveRedisIns.zrevrangeByScoreWithScores(key, max, min, offset, count);

    }

    @Override
    public <T extends Serializable> T hget(byte[] key, String field) {
        return slaveRedisIns.hget(key, field);

    }

    @Override
    public <T extends Serializable> List<T> hmget(byte[] key, String... fields) {
        return slaveRedisIns.hmget(key, fields);
    }

    @Override
    public <T extends Serializable> Map<String, T> hgetall(byte[] key) {
        return slaveRedisIns.hgetall(key);
    }

    @Override
    public <T extends Serializable> T lindex(byte[] key, int index) {
        return slaveRedisIns.lindex(key, index);

    }

    @Override
    public <T extends Serializable> List<T> lrange(byte[] key, int start, int end) {
        return slaveRedisIns.lrange(key, start, end);

    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        return slaveRedisIns.zrange(key, start, end);

    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        return slaveRedisIns.zrevrange(key, start, end);
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        return slaveRedisIns.zrangeWithScores(key, start, end);

    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        return slaveRedisIns.zrevrangeWithScores(key, start, end);
    }

    @Override
    public Long zcount(String key, String min, String max) {
        return slaveRedisIns.zcount(key, min, max);
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {
        return slaveRedisIns.zrangeByScore(key, min, max);

    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        return slaveRedisIns.zrevrangeByScore(key, max, min);

    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        return slaveRedisIns.zrangeByScore(key, min, max, offset, count);
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        return slaveRedisIns.zrevrangeByScore(key, max, min, offset, count);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        return slaveRedisIns.zrangeByScoreWithScores(key, min, max);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        return slaveRedisIns.zrevrangeByScoreWithScores(key, max, min);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        return slaveRedisIns.zrangeByScoreWithScores(key, min, max, offset, count);

    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        return slaveRedisIns.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Override
    public <T extends Serializable> Set<T> zrangeByScoreT(String key, double min, double max) {
        return slaveRedisIns.zrangeByScoreT(key, min, max);
    }

    @Override
    public <T extends Serializable> Set<T> zrangeByScoreT(String key, double min, double max, int offset, int count) {
        return slaveRedisIns.zrangeByScoreT(key, min, max, offset, count);
    }

    @Override
    public <T extends Serializable> Set<T> zrevrangeByScoreT(String key, double min, double max) {
        return slaveRedisIns.zrevrangeByScoreT(key, min, max);
    }

    @Override
    public <T extends Serializable> Set<T> zrevrangeByScoreT(String key, double min, double max, int offset, int count) {
        return slaveRedisIns.zrevrangeByScoreT(key, min, max, offset, count);
    }

    @Override
    public Long strlen(final String key) {
        return slaveRedisIns.strlen(key);
    }

    @Override
    public String echo(final String string) {
        return slaveRedisIns.echo(string);
    }

    @Override
    public Long bitcount(final String key) {
        return slaveRedisIns.bitcount(key);
    }

    @Override
    public Long bitcount(final String key, final long start, final long end) {
        return slaveRedisIns.bitcount(key, start, end);
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(final String key, final int cursor) {
        return slaveRedisIns.hscan(key, cursor);
    }

    @Override
    public ScanResult<String> sscan(final String key, final int cursor) {
        return slaveRedisIns.sscan(key, cursor);
    }

    @Override
    public ScanResult<Tuple> zscan(final String key, final int cursor) {
        return slaveRedisIns.zscan(key, cursor);
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
        return slaveRedisIns.hscan(key, cursor);
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor) {
        return slaveRedisIns.sscan(key, cursor);
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor) {
        return slaveRedisIns.zscan(key, cursor);
    }

    @Override
    public long pfcount(String key) {
        return slaveRedisIns.pfcount(key);
    }

    public SuishenRedis getSlaveRedisIns() {
        return slaveRedisIns;
    }
}
