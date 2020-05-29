/**
 * 
 */
package org.jim.core.cache.redis;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.jim.core.cache.caffeineredis.CaffeineRedisCache;
import org.jim.core.cache.caffeineredis.CaffeineRedisCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jim.core.cache.CacheChangeType;
import org.jim.core.cache.CacheChangedVo;
import redis.clients.jedis.JedisPubSub;

/**
 * 
 * @author WChao
 * @date 2018年3月8日 下午1:00:02
 */
public class JedisSubscriber extends JedisPubSub {
	
	private static Logger log = LoggerFactory.getLogger(JedisSubscriber.class);
	
	@Override
	public void onMessage(String channel, String message) {
		log.debug(String.format("接收到redis发布的消息,chanel %s,message %s",channel,message));
		String[] cacheChanges = message.split(":");
		String cacheName = cacheChanges[0];
		String key = cacheChanges[1];
		CacheChangeType type = CacheChangeType.from(Integer.valueOf(cacheChanges[2]));
		String clientid = cacheChanges[3];
		if (StringUtils.isBlank(clientid)) {
			log.error("clientid is null");
			return;
		}
		if (Objects.equals(CacheChangedVo.CLIENTID, clientid)) {
			log.debug("自己发布的消息,{}", clientid);
			return;
		}
		CaffeineRedisCache caffeineRedisCache = CaffeineRedisCacheManager.getCache(cacheName);
		if (caffeineRedisCache == null) {
			log.debug("不能根据cacheName[{}]找到CaffeineRedisCache对象", cacheName);
			return;
		}
		if (type == CacheChangeType.PUT  || type == CacheChangeType.UPDATE || type == CacheChangeType.REMOVE) {
			caffeineRedisCache.getCaffeineCache().remove(key);
		} else if (type == CacheChangeType.CLEAR) {
			caffeineRedisCache.getCaffeineCache().clear();
		}
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		log.debug("订阅 redis 通道成功, channel {} , subscribedChannels {}",channel,subscribedChannels);
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		log.debug(String.format("取消订阅 redis通道 , channel %s , subscribeChannels %d",channel,subscribedChannels));
	}
	
	
}
