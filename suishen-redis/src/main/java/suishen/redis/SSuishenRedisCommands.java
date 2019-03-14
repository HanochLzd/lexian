package suishen.redis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.JedisCommands;

public interface SSuishenRedisCommands extends JedisCommands {
	
	public Long del(String key);

	public <T extends Serializable> String set(byte[] key, T object);
	
	public <T extends Serializable> String setex(byte[] key, int seconds, T object);
	
	public <T extends Serializable> Long setnx(byte[] key, T object); 
	
	public <T extends Serializable> T get(byte[] key);
	
	public <T extends Serializable> T getSet(byte[] key, T object);
	
	public <T extends Serializable> Long lpush(byte[] key, T object); 
	
	public <T extends Serializable> Long rpush(byte[] key, T object);  
	
	public <T extends Serializable> T lindex(byte[] key, int index);
	
	public <T extends  Serializable> T lpop(byte[] key);
	
	public <T extends Serializable> T rpop(byte[] key);
	
	public <T extends Serializable> String lset(byte[] key, int index, T object);
	
	public <T extends Serializable> List<T> lrange(byte[] key, int start, int end);
	
	public <T extends Serializable> long lrem(byte[] key, int count, T object);
	
	public <T extends Serializable> Long hset(byte[] key, String field, T object);  
	
	public <T extends Serializable> T hget(byte[] key, String field);
	
	public <T extends Serializable> List<T> hmget(byte[] key, String... fields);
	
	public <T extends Serializable> Map<String, T> hgetall(byte[] key);
	
	public <T extends Serializable> Long sadd(byte[] key, T object);
	
	public <T extends Serializable> boolean sismember(byte[] key, T object);
	
	public <T extends Serializable> T spop(byte[] key);
	
	public <T extends Serializable> T srandmember(byte[] key);
	
	public <T extends Serializable> Long srem(byte[] key, T member);
	
}
