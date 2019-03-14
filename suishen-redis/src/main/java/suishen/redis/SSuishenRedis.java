package suishen.redis;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

public class SSuishenRedis {

	private RedisTemplate redisTemplate;
	private int expireTime = -1;

    protected SSuishenRedis(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	protected SSuishenRedis(RedisTemplate redisTemplate, int expireTime) {
		this.redisTemplate = redisTemplate;
		this.expireTime = expireTime;
	}

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }


	public void del(String key) {
		redisTemplate.delete(key);
	}

	public void set(String key, Object value) {
		ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
		valueOper.set(key, value);
	}

	private RedisTemplate getClient() {
		return redisTemplate;
	}

	public <T extends Serializable> T get(String key) {
		ValueOperations<String, T> valueOper = redisTemplate.opsForValue();
		return valueOper.get(key);
	}

	public <T extends Serializable> T getSet(String key, Object object) {
		ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
		return (T)valueOper.getAndSet(key, object);
	}

	public Long expire(String key, int seconds) {
		if (redisTemplate.expire(key, seconds, TimeUnit.SECONDS)) {
			return new Long(seconds);
		} else {
			return new Long(-1);
		}
	}

	public boolean expireAt(String key, long unixTime) {
		return redisTemplate.expireAt(key, new Date(unixTime));
	}

	public Long ttl(String key) {
		return redisTemplate.getExpire(key);
	}

/*	public void setex(String key, int seconds, String value) {
		ValueOperations<String, String> valueOper = redisTemplate.opsForValue();
		valueOper.set(key, value, seconds, TimeUnit.SECONDS);
	}*/

	public <T extends Serializable> void setex(String key, int seconds, Object object) {
		ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
		valueOper.set(key, object, seconds, TimeUnit.SECONDS);
	}

	public Long decrBy(String key, long delta) {
		RedisAtomicLong counterLong = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
		return counterLong.addAndGet(delta * -1);
	}

	public Long decr(String key) {
		RedisAtomicLong counterLong = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
		return counterLong.decrementAndGet();
	}

	public Long incrBy(String key, long delta) {
		RedisAtomicLong counterLong = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
		return counterLong.addAndGet(delta);
	}

	public Long incr(String key) {
		RedisAtomicLong counterLong = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
		return counterLong.incrementAndGet();
	}

	/*public <T extends Serializable> void set(String key, T object) {
		ValueOperations<String, T> valueOper = redisTemplate.opsForValue();
		valueOper.set(key, object);
	}*/

	public void quit() {
		redisTemplate.getConnectionFactory().getConnection().close();
	}

	public <T extends Serializable> long rpush(String key, Object object) {
		ListOperations<String, Object> val = redisTemplate.opsForList();
		if (val == null) {
			return -1;
		} else {
			return val.rightPush(key, object);
		}
	}
	
	public <T extends Serializable> long addAll(String key, List<Object> objects) {
		ListOperations<String, Object> val = redisTemplate.opsForList();
		if (val == null) {
			return -1;
		} else {
			return val.rightPushAll(key, objects);
		}
	}

	public <T extends Serializable> int llen(String key) {
		ListOperations<String, T> val = redisTemplate.opsForList();
		if (val == null) {
			return -1;
		} else {
			return (int) val.size(key).intValue();
		}
	}

	public <T extends Serializable> T lindex(String key, int index) {
		ListOperations<String, T> val = redisTemplate.opsForList();
		if (val == null) {
			return null;
		} else {
			return val.index(key, index);
		}
	}

	public <T extends Serializable> List<T> lrange(String key, int start, int end) {
		ListOperations<String, T> val = redisTemplate.opsForList();
		if (val == null) {
			return Collections.EMPTY_LIST;
		} else {
			return val.range(key, start, end);
		}
	}

	public boolean exists(String key) {
		return redisTemplate.hasKey(key);
	}

	public <T extends Serializable> boolean lset(String key, int index, Object object) {
		ListOperations<String, Object> val = redisTemplate.opsForList();
		if (val == null) {
			return false;
		} else {
			val.set(key, index, object);
			return true;
		}
	}

	public <T extends Serializable> long lrem(String key, int i, Object object) {
		ListOperations<String, T> val = redisTemplate.opsForList();
		if (val == null) {
			return -1;
		} else {
			return val.remove(key, i, object);
		}
	}

/*	public long rpush(String key, String value) {
		ListOperations<String, String> val = redisTemplate.opsForList();
		if (val == null) {
			return -1;
		} else {
			return val.rightPush(key, value);
		}
	}*/

	public List<String> lrange(String key, long start, long end) {
		ListOperations<String, String> val = redisTemplate.opsForList();
		if (val == null) {
			return Collections.EMPTY_LIST;
		} else {
			return val.range(key, start, end);
		}
	}

	public long getCounter(String key) {
		return new RedisAtomicLong(key, redisTemplate.getConnectionFactory()).get();
	}

}
