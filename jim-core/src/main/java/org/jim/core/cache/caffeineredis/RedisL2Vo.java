package org.jim.core.cache.caffeineredis;

import java.io.Serializable;

import org.jim.core.cache.redis.RedisCache;

/**
 * @author WChao
 * @date 2018年3月13日 下午8:05:50
 */
public class RedisL2Vo {
	
	private RedisCache redisCache;
	private String key;
	private Serializable value;
	
	public RedisL2Vo(RedisCache redisCache , String key , Serializable value){
		this.redisCache = redisCache;
		this.key = key;
		this.value = value;
	}

	public RedisCache getRedisCache() {
		return redisCache;
	}

	public void setRedisCache(RedisCache redisCache) {
		this.redisCache = redisCache;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Serializable getValue() {
		return value;
	}

	public void setValue(Serializable value) {
		this.value = value;
	}
	
}
