package suishen.redis;

import org.apache.log4j.Logger;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import suishen.redis.util.CodeUtil;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

public class SuishenRedis implements SuishenRedisCommands {

    private JedisPool jedisPool;

    private static final Logger logger = Logger.getLogger(SuishenRedis.class);

    private static final int DEFAULT_PORT = 6379;

    protected static final int DEFAULT_TIMEOUT = 3000;

    public SuishenRedis(String ip) {
        this(ip, DEFAULT_PORT);
    }

    public SuishenRedis(String ip, int port) {
        jedisPool = new JedisPool(getDefaultJedisPoolConfig(), ip, port, DEFAULT_TIMEOUT);
    }

    public SuishenRedis(String cacheAdd, JedisPoolConfig jconfig) {
        this(cacheAdd, jconfig, DEFAULT_TIMEOUT);
    }

    private List<String> redisHostList = new ArrayList<String>();

    public SuishenRedis(String cacheAdd, JedisPoolConfig jconfig, int timeout) {
        if (cacheAdd == null || cacheAdd.length() <= 0) {
            logger.error(" init pool, error adds is null: ");
            return;
        }
        redisHostList = new ArrayList<String>(Arrays.asList(cacheAdd.split("\\s")));

        initPool(jconfig, getHost(), timeout);

    }


    private JedisPoolConfig getDefaultJedisPoolConfig() {
        JedisPoolConfig jconfig = new JedisPoolConfig();
        jconfig.setMaxIdle(20);
        jconfig.setTestOnBorrow(true);
        jconfig.setMaxTotal(100);
        jconfig.setMaxWaitMillis(1000);

        // jedis检测连接时会发ping，但nutcracker不支持，会导致该连接立即被关掉，因此把检测禁用掉
        jconfig.setTestWhileIdle(false);

        return jconfig;
    }

    private String[] getHost() {
        Random rand = new Random();
        String host = redisHostList.get(rand.nextInt(redisHostList.size()));
        return host.split(":");
    }

    private void initPool(JedisPoolConfig jconfig, String[] host, int timeout) {
        String ip = host[0];
        int port = Integer.valueOf(host[1]);
        try {
            jconfig.setTestWhileIdle(false);
            jedisPool = new JedisPool(jconfig, ip, port, timeout);
        } catch (Exception e) {
            logger.error("Failed to init JedisPool,try next config", e);
            initPool(jconfig, getHost(), timeout);
        }
    }

    public void onFinally(Jedis jedis) {
        if (jedis != null) {
            // jedis is multi ,need discard all opes
            if (jedis.getClient().isInMulti()) {
                jedis.getClient().discard();
                jedis.disconnect();
            }
            if (jedisPool != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public void onBrokenFinally(Jedis jedis) {
        if (jedis != null) {
            // jedis is multi ,need discard all opes
//            if (jedis.getClient().isInMulti()) {
//                jedis.getClient().discard();
//                jedis.disconnect();
//            }
            if (jedisPool != null) {
                jedisPool.returnBrokenResource(jedis);
            }
        }
    }

    public void onException(Jedis jedis) {
        onFinally(jedis);
    }

    /**
     * 关闭客户端
     */
    public void quit() {
        jedisPool.destroy();
    }

    public Jedis getClient() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (JedisConnectionException e) {
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
        }
        return jedis;
    }

    /**
     * 判断redis连接是否存活
     *
     * @return
     */
    public boolean isAlive() {
        boolean ret = false;

        Jedis jedis = getClient();
        if (jedis != null) {
            ret = true;
            jedisPool.returnResource(jedis);
        }

        return ret;
    }

    @Override
    public Long del(String key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();
            return jedis.del(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long del(byte[] key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();
            return jedis.del(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String set(String key, String value) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();
            return jedis.set(key, value);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, long time) {
        return null;
    }

    @Override
    public String get(String key) {
        Jedis jedis = null;
        // boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (JedisConnectionException e) {
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public <T extends Serializable> T get(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] serialDate = jedis.get(key);
            return CodeUtil.objectDecode(serialDate);
        } catch (JedisConnectionException e) {
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            jedis = jedisPool.getResource();
            byte[] serialDate = jedis.get(key);
            return CodeUtil.objectDecode(serialDate);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public <T extends Serializable> T getSet(byte[] key, T object) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();
            byte[] serialDate = jedis.getSet(key, CodeUtil.objectEncode(object));
            return CodeUtil.objectDecode(serialDate);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Boolean exists(String key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.exists(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String type(String key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.type(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long expire(String key, int seconds) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();
            return jedis.expire(key, seconds);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.expireAt(key, unixTime);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long ttl(String key) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.ttl(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Boolean setbit(String key, long offset, boolean value) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.setbit(key, offset, value);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Boolean getbit(String key, long offset) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.getbit(key, offset);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long setrange(String key, long offset, String value) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.setrange(key, offset, value);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public String getrange(String key, long startOffset, long endOffset) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.getrange(key, startOffset, endOffset);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String getSet(String key, String value) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.getSet(key, value);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long setnx(String key, String value) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.setnx(key, value);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public <T extends Serializable> Long setnx(byte[] key, T object) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.setnx(key, CodeUtil.objectEncode(object));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String setex(String key, int seconds, String value) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.setex(key, seconds, value);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public <T extends Serializable> String setex(byte[] key, int seconds, T object) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.setex(key, seconds, CodeUtil.objectEncode(object));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long decrBy(String key, long integer) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.decrBy(key, integer);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long decr(String key) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.decr(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long incrBy(String key, long integer) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.incrBy(key, integer);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long incr(String key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.incr(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long append(String key, String value) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.append(key, value);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public String substr(String key, int start, int end) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.substr(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long hset(String key, String field, String value) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hset(key, field, value);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String hget(String key, String field) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hget(key, field);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long hsetnx(String key, String field, String value) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hsetnx(key, field, value);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hmset(key, hash);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public List<String> hmget(String key, String... fields) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hmget(key, fields);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long hincrBy(String key, String field, long value) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hincrBy(key, field, value);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Boolean hexists(String key, String field) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hexists(key, field);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public Long hdel(String key, String field) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hdel(key, field);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long hlen(String key) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hlen(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Set<String> hkeys(String key) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hkeys(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public List<String> hvals(String key) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hvals(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Map<String, String> hgetAll(String key) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hgetAll(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    public Long rpush(String key, String string) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.rpush(key, string);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    public Long lpush(String key, String string) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.lpush(key, string);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long llen(String key) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.llen(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.lrange(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public String ltrim(String key, long start, long end) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.ltrim(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String lindex(String key, long index) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.lindex(key, index);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public <T extends Serializable> String lset(byte[] key, int index, T object) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.lset(key, index, CodeUtil.objectEncode(object));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String lset(String key, long index, String value) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.lset(key, index, value);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long lrem(String key, long count, String value) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.lrem(key, count, value);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public <T extends Serializable> long lrem(byte[] key, int count, T object) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.lrem(key, count, CodeUtil.objectEncode(object));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return -1;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public String lpop(String key) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.lpop(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public String rpop(String key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.rpop(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    public Long sadd(String key, String member) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.sadd(key, member);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<String> smembers(String key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.smembers(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    public Long srem(String key, String member) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.srem(key, member);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> Long srem(byte[] key, T member) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.srem(key, CodeUtil.objectEncode(member));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public String spop(String key) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.spop(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long scard(String key) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.scard(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Boolean sismember(String key, String member) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.sismember(key, member);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> boolean sismember(byte[] key, T object) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.sismember(key, CodeUtil.objectEncode(object));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return false;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public String srandmember(String key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.srandmember(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public List<String> srandmember(String key, int count) {
        return null;
    }

    @Override
    public <T extends Serializable> T srandmember(byte[] key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            byte[] serialDate = jedis.srandmember(key);
            return CodeUtil.objectDecode(serialDate);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long zadd(String key, double score, String member) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zadd(key, score, member);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> Long zadd(String key, double score, T object) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();
            return jedis.zadd(CodeUtil.keyEncode(key), score, CodeUtil.objectEncode(object));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    public Set<String> zrange(String key, int start, int end) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrange(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public Long zrem(String key, String member) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrem(key, member);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Double zincrby(String key, double score, String member) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zincrby(key, score, member);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long zrank(String key, String member) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrank(key, member);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long zrevrank(String key, String member) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrevrank(key, member);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    public Set<String> zrevrange(String key, int start, int end) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrevrange(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    public Set<Tuple> zrangeWithScores(String key, int start, int end) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrangeWithScores(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrevrangeWithScores(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long zcard(String key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zcard(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Double zscore(String key, String member) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zscore(key, member);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public List<String> sort(String key) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.sort(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public List<String> sort(String key, SortingParams sortingParameters) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.sort(key, sortingParameters);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long zcount(String key, double min, double max) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zcount(key, min, max);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrangeByScore(key, min, max);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrevrangeByScore(key, max, min);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> Set<T> zrevrangeByScoreT(String key, double min, double max) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            Set<byte[]> serialDate = jedis.zrevrangeByScore(CodeUtil.keyEncode(key), min, max);
            Set<T> res = new LinkedHashSet<T>();

            for (byte[] e : serialDate) {
                res.add((T) CodeUtil.objectDecode(e));
            }

            return res;
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrangeByScore(key, min, max, offset, count);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrevrangeByScore(key, max, min, offset, count);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> Set<T> zrevrangeByScoreT(String key, double min, double max, int offset, int count) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            Set<byte[]> serialDate = jedis.zrevrangeByScore(CodeUtil.keyEncode(key), min, max, offset, count);

            Set<T> res = new LinkedHashSet<T>();

            for (byte[] e : serialDate) {
                res.add((T) CodeUtil.objectDecode(e));
            }

            return res;
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrangeByScoreWithScores(key, min, max);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrevrangeByScoreWithScores(key, max, min);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    public Long zremrangeByRank(String key, int start, int end) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zremrangeByRank(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long zremrangeByScore(String key, double start, double end) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zremrangeByScore(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    public Long zinterstore(String dstkey, ZParams params, String... keySets) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zinterstore(dstkey, params, keySets);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public Long zunionstore(String dstkey, ZParams params, String... keySets) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zunionstore(dstkey, params, keySets);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long linsert(String key, LIST_POSITION where, String pivot, String value) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.linsert(key, where, pivot, value);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> String set(byte[] key, T object) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.set(key, CodeUtil.objectEncode(object));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> Long lpush(byte[] key, T object) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.lpush(key, CodeUtil.objectEncode(object));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> Long rpush(byte[] key, T object) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.rpush(key, CodeUtil.objectEncode(object));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> Long hset(byte[] key, String field, T object) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hset(key, CodeUtil.keyEncode(field), CodeUtil.objectEncode(object));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> T hget(byte[] key, String field) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            byte[] serialDate = jedis.hget(key, CodeUtil.keyEncode(field));

            return CodeUtil.objectDecode(serialDate);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> List<T> hmget(byte[] key, String... fields) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            List<byte[]> serialDate = jedis.hmget(key, CodeUtil.keyEncode(fields));

            List<T> res = new ArrayList<T>();

            for (byte[] serial : serialDate) {
                res.add((T) CodeUtil.objectDecode(serial));
            }

            return res;
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> Map<String, T> hgetall(byte[] key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            Map<byte[], byte[]> serialDate = jedis.hgetAll(key);

            Map<String, T> res = new HashMap<String, T>();

            for (Entry<byte[], byte[]> e : serialDate.entrySet()) {
                res.put(new String(e.getKey()), (T) CodeUtil.objectDecode(e.getValue()));
            }

            return res;
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> T lindex(byte[] key, int index) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            byte[] serialDate = jedis.lindex(key, index);

            return CodeUtil.objectDecode(serialDate);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> T lpop(byte[] key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            byte[] serialDate = jedis.lpop(key);

            return CodeUtil.objectDecode(serialDate);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> T rpop(byte[] key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            byte[] serialDate = jedis.rpop(key);

            return CodeUtil.objectDecode(serialDate);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> List<T> lrange(byte[] key, int start, int end) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            List<byte[]> serialDateList = jedis.lrange(key, start, end);

            List<T> objList = new ArrayList<T>();

            for (byte[] serialDate : serialDateList) {
                objList.add((T) CodeUtil.objectDecode(serialDate));
            }

            return objList;
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> Long sadd(byte[] key, T object) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.sadd(key, CodeUtil.objectEncode(object));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> T spop(byte[] key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            byte[] serialDate = jedis.spop(key);
            return CodeUtil.objectDecode(serialDate);

        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long hdel(String key, String... field) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hdel(key, field);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long rpush(String key, String... string) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.rpush(key, string);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long lpush(String key, String... string) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.lpush(key, string);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long sadd(String key, String... member) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.sadd(key, member);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long srem(String key, String... member) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.srem(key, member);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zadd(key, scoreMembers);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrange(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long zrem(String key, String... member) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrem(key, member);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrevrange(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrangeByScoreWithScores(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrevrangeWithScores(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long zcount(String key, String min, String max) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zcount(key, min, max);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrangeByScore(key, min, max);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public <T extends Serializable> Set<T> zrangeByScoreT(String key, double min, double max) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            Set<byte[]> serialDate = jedis.zrangeByScore(CodeUtil.keyEncode(key), min, max);

            Set<T> res = new LinkedHashSet<T>();

            for (byte[] e : serialDate) {
                res.add((T) CodeUtil.objectDecode(e));
            }

            return res;
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrevrangeByScore(key, max, min);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrangeByScore(key, min, max, offset, count);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    public <T extends Serializable> Set<T> zrangeByScoreT(String key, double min, double max, int offset, int count) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            Set<byte[]> serialDate = jedis.zrangeByScore(CodeUtil.keyEncode(key), min, max, offset, count);

            Set<T> res = new LinkedHashSet<T>();

            for (byte[] e : serialDate) {
                res.add((T) CodeUtil.objectDecode(e));
            }

            return res;
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrevrangeByScore(key, max, min, offset, count);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrangeByScoreWithScores(key, min, max);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrevrangeByScoreWithScores(key, max, min);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long zremrangeByRank(String key, long start, long end) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zremrangeByRank(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long zremrangeByScore(String key, String start, String end) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zremrangeByScore(key, start, end);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long zlexcount(String key, String min, String max) {
        return null;
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max) {
        return null;
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        return null;
    }

    @Override
    public Long zremrangeByLex(String key, String min, String max) {
        return null;
    }

    @Override
    public Long lpushx(String key, String... string) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.lpushx(key, string);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long rpushx(String key, String... string) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.rpushx(key, string);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long persist(String key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.persist(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Boolean setbit(final String key, final long offset, final String value) {
        return new SuishenRedisExecutor<Boolean>().exe(new SuishenRedisRunner<Boolean>() {
            @Override
            public Boolean process(Jedis jedis) throws JedisConnectionException {
                return jedis.setbit(key, offset, value);
            }
        }, jedisPool);
    }

    @Override
    public Long strlen(final String key) {
        return new SuishenRedisExecutor<Long>().exe(new SuishenRedisRunner<Long>() {
            @Override
            public Long process(Jedis jedis) throws JedisConnectionException {
                return jedis.strlen(key);
            }
        }, jedisPool);
    }

    @Override
    public List<String> blpop(final String arg) {
        return new SuishenRedisExecutor<List<String>>().exe(new SuishenRedisRunner<List<String>>() {
            @Override
            public List<String> process(Jedis jedis) throws JedisConnectionException {
                return jedis.blpop(arg);
            }
        }, jedisPool);
    }

    @Override
    public List<String> blpop(final int timeout, final String key) {
        return new SuishenRedisExecutor<List<String>>().exe(new SuishenRedisRunner<List<String>>() {
            @Override
            public List<String> process(Jedis jedis) throws JedisConnectionException {
                return jedis.blpop(timeout, key);
            }
        }, jedisPool);
    }

    @Override
    public List<String> brpop(final String arg) {
        return new SuishenRedisExecutor<List<String>>().exe(new SuishenRedisRunner<List<String>>() {
            @Override
            public List<String> process(Jedis jedis) throws JedisConnectionException {
                return jedis.brpop(arg);
            }
        }, jedisPool);
    }

    @Override
    public List<String> brpop(final int timeout, final String key) {
        return new SuishenRedisExecutor<List<String>>().exe(new SuishenRedisRunner<List<String>>() {
            @Override
            public List<String> process(Jedis jedis) throws JedisConnectionException {
                return jedis.blpop(timeout, key);
            }
        }, jedisPool);
    }

    @Override
    public String echo(final String string) {
        return new SuishenRedisExecutor<String>().exe(new SuishenRedisRunner<String>() {
            @Override
            public String process(Jedis jedis) throws JedisConnectionException {
                return jedis.echo(string);
            }
        }, jedisPool);
    }

    @Override
    public Long move(final String key, final int dbIndex) {
        return new SuishenRedisExecutor<Long>().exe(new SuishenRedisRunner<Long>() {
            @Override
            public Long process(Jedis jedis) throws JedisConnectionException {
                return jedis.move(key, dbIndex);
            }
        }, jedisPool);
    }

    @Override
    public Long bitcount(final String key) {
        return new SuishenRedisExecutor<Long>().exe(new SuishenRedisRunner<Long>() {
            @Override
            public Long process(Jedis jedis) throws JedisConnectionException {
                return jedis.bitcount(key);
            }
        }, jedisPool);
    }

    @Override
    public Long bitcount(final String key, final long start, final long end) {
        return new SuishenRedisExecutor<Long>().exe(new SuishenRedisRunner<Long>() {
            @Override
            public Long process(Jedis jedis) throws JedisConnectionException {
                return jedis.bitcount(key, start, end);
            }
        }, jedisPool);
    }

    @Override
    public ScanResult<Entry<String, String>> hscan(final String key, final int cursor) {
        return new SuishenRedisExecutor<ScanResult<Entry<String, String>>>().exe(new SuishenRedisRunner<ScanResult<Entry<String, String>>>() {
            @Override
            public ScanResult<Entry<String, String>> process(Jedis jedis) throws JedisConnectionException {
                return jedis.hscan(key, cursor);
            }
        }, jedisPool);
    }

    @Override
    public ScanResult<String> sscan(final String key, final int cursor) {
        return new SuishenRedisExecutor<ScanResult<String>>().exe(new SuishenRedisRunner<ScanResult<String>>() {
            @Override
            public ScanResult<String> process(Jedis jedis) throws JedisConnectionException {
                return jedis.sscan(key, cursor);
            }
        }, jedisPool);
    }

    @Override
    public ScanResult<Tuple> zscan(final String key, final int cursor) {
        return new SuishenRedisExecutor<ScanResult<Tuple>>().exe(new SuishenRedisRunner<ScanResult<Tuple>>() {
            @Override
            public ScanResult<Tuple> process(Jedis jedis) throws JedisConnectionException {
                return jedis.zscan(key, cursor);
            }
        }, jedisPool);
    }

    @Override
    public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.hscan(key, cursor);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.sscan(key, cursor);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.zscan(key, cursor);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long pfadd(String key, String... elements) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.pfadd(key, elements);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return null;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public long pfcount(String key) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();

            return jedis.pfcount(key);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
            }
            return 0;
        } finally {
            if (borrowOrOprSuccess) {
                jedisPool.returnResource(jedis);
            }
        }
    }
}
