/**
 * 
 */
package org.jim.server.cluster.redis;

import org.apache.commons.lang3.StringUtils;
import org.jim.core.ImConst;
import org.jim.core.ImPacket;
import org.jim.core.cluster.ImClusterConfig;
import org.jim.core.cluster.ImClusterVO;
import org.jim.server.JimServerAPI;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Tio;
import org.tio.utils.json.Json;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @desc Redis集群配置
 * @author WChao
 * @date 2020-05-01
 */
public class RedisClusterConfig extends ImClusterConfig implements ImConst {
	
	private static Logger log = LoggerFactory.getLogger(RedisClusterConfig.class);
	/**
	 * 集群主题后缀
	 */
	private String topicSuffix;
	/**
	 * 集群订阅主题
	 */
	private String topic;
	/**
	 * 客户端
	 */
	private RedissonClient redissonClient;
	/**
	 * Redis发布/订阅Topic
	 */
	public RTopic<ImClusterVO> rTopic;
	
	/**
	 * 收到了多少次topic
	 */
	public static final AtomicLong RECEIVED_TOPIC_COUNT = new AtomicLong();
	
	/**
	 * J-IM内置的集群是用redis的topic来实现的，所以不同机器就要有一个不同的topicSuffix
	 * @param topicSuffix 不同类型的就要有一个不同的topicSuffix
	 * @param redissonClient redis客户端
	 * @return
	 * @author: WChao
	 */
	public static RedisClusterConfig newInstance(String topicSuffix, RedissonClient redissonClient) {
		if (redissonClient == null) {
			throw new RuntimeException(RedissonClient.class.getSimpleName() + "不允许为空");
		}
		RedisClusterConfig me = new RedisClusterConfig(topicSuffix, redissonClient);
		me.rTopic = redissonClient.getTopic(me.topic);
		me.rTopic.addListener((String channel, ImClusterVO imClusterVo) -> {
			log.info("收到topic:{}, count:{}, ImClusterVo:{}", channel, RECEIVED_TOPIC_COUNT.incrementAndGet(), Json.toJson(imClusterVo));
			String clientId = imClusterVo.getClientId();
			if (StringUtils.isBlank(clientId)) {
				log.error("clientId is null");
				return;
			}
			if (Objects.equals(ImClusterVO.CLIENT_ID, clientId)) {
				log.info("message received by this machine are ignored, {}", clientId);
				return;
			}
			ImPacket packet = imClusterVo.getPacket();
			if (packet == null) {
				log.error("packet is null");
				return;
			}
			packet.setFromCluster(true);
			//发送给所有
			boolean isToAll = imClusterVo.isToAll();
			if (isToAll) {
				Tio.sendToAll(null, packet);
			}
			//发送给指定组
			String group = imClusterVo.getGroup();
			if (StringUtils.isNotBlank(group)) {
				JimServerAPI.sendToGroup(group, packet);
			}
			//发送给指定用户
			String userId = imClusterVo.getUserId();
			if (StringUtils.isNotBlank(userId)) {
				JimServerAPI.sendToUser(userId, packet);
			}
			//发送给指定token
			String token = imClusterVo.getToken();
			if (StringUtils.isNotBlank(token)) {
				//Tio.sendToToken(me.groupContext, token, packet);
			}
			//发送给指定ip
			String ip = imClusterVo.getIp();
			if (StringUtils.isNotBlank(ip)) {
				JimServerAPI.sendToIp(ip, packet);
			}
		});
		return me;
	}
	private RedisClusterConfig(String topicSuffix, RedissonClient redissonClient) {
		this.setTopicSuffix(topicSuffix);
		this.setRedissonClient(redissonClient);
	}

	@Override
	public void send(ImClusterVO imClusterVo) {
		rTopic.publish(imClusterVo);
	}
	@Override
	public void sendAsync(ImClusterVO imClusterVo) {
		rTopic.publishAsync(imClusterVo);
	}

	public String getTopicSuffix() {
		return topicSuffix;
	}

	public void setTopicSuffix(String topicSuffix) {
		this.topicSuffix = topicSuffix;
		this.topic = topicSuffix + Topic.JIM_CLUSTER_TOPIC;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public RedissonClient getRedissonClient() {
		return redissonClient;
	}

	public void setRedissonClient(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	public RTopic<ImClusterVO> getRTopic() {
		return rTopic;
	}

	public void setRTopic(RTopic<ImClusterVO> rTopic) {
		this.rTopic = rTopic;
	}
}
