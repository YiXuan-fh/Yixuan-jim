package org.jim.core.cache.redis;

import java.io.Serializable;

import org.jim.core.ImConst;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author WChao
 * @date 2018年5月18日 下午2:46:55
 */
public class RedissonTemplate implements Serializable{

	private static final long serialVersionUID = -4528751601700736437L;
	private static final Logger logger = LoggerFactory.getLogger(RedissonTemplate.class);
	private static volatile RedissonTemplate instance = null;
	private static RedisConfiguration redisConfig = null;
	private static final String REDIS = "redis";
	private static RedissonClient redissonClient = null;
	
	private RedissonTemplate(){};
	
	public static RedissonTemplate me() throws Exception{
		 if (instance == null) { 
	        	synchronized (RedissonTemplate.class) {
					if(instance == null){
						redisConfig = RedisConfigurationFactory.parseConfiguration();
						init();
						instance = new RedissonTemplate();
					}
				}
	     }
		 return instance;
	}
	
	private static final void init() throws Exception {
			String host = redisConfig.getHost();
			logger.info("connect redis[{}]", ImConst.JIM);
			if(host == null) {
				logger.error("the server ip of redis  must be not null!");
				throw new Exception("the server ip of redis  must be not null!");
			}	
			int port = redisConfig.getPort();
			String password = redisConfig.getAuth();
			Config redissonConfig = new Config();
			SingleServerConfig singleServerConfig = redissonConfig.useSingleServer();
			singleServerConfig.setAddress(REDIS+"://"+host+":"+port).setPassword(password).setTimeout(redisConfig.getTimeout()).setRetryAttempts(redisConfig.getRetryNum());
			try {
			   redissonClient = Redisson.create(redissonConfig);
			} catch (Exception e) {
				logger.error("can't create RedissonClient for server"+redisConfig.getHost());
				throw new Exception("can't create RedissonClient for server"+redisConfig.getHost());
			}
			
	}
	/**
	 * 获取RedissonClient客户端;
	 * @return
	 */
	public final RedissonClient getRedissonClient(){
		return redissonClient;
	}
}
