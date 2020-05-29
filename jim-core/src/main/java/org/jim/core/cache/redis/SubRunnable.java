package org.jim.core.cache.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

/**
 * @author WChao
 * @date 2018年3月8日 下午1:07:55
 */
public class SubRunnable implements Runnable {
	
	Logger log = LoggerFactory.getLogger(SubRunnable.class);
	private  String subChannel = null;
	private Jedis jedis = null;
	private JedisSubscriber subscriber = new JedisSubscriber();
	public SubRunnable(String subChannel){
		if(StringUtils.isEmpty(subChannel)){
			throw new RuntimeException("chanel通道异常!");
		}
		this.subChannel = subChannel;
	}
	@Override
	public void run() {
		log.debug("订阅 redis , chanel {} , 线程将阻塞",subChannel);
		while(true){
			try {
				jedis = JedisTemplate.me().getJedis();
				if(jedis != null){
					 jedis.subscribe(subscriber, subChannel);
				}
			} catch (Exception e) {
				log.error(e.toString(),e);
				log.error("订阅线程异常,重新获取订阅客户端连接...");
			}finally {
				try {
					JedisTemplate.me().close(jedis);
				} catch (Exception e) {
					log.error(e.toString(),e);
				}
			}
		}
	}
}
