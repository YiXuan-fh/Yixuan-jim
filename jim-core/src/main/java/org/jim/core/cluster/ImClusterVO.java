package org.jim.core.cluster;

import org.jim.core.ImPacket;

import java.util.UUID;

/**
 * 成员变量group, userId, ip谁有值就发给谁，toAll为true则发给所有<br>
 * packet是不允许为null的
 * @author WChao 
 * 2018年05月20日 下午3:10:29
 */
public class ImClusterVO implements java.io.Serializable {

	private static final long serialVersionUID = 6978027913776155664L;
	/**
	 * 集群各个机器唯一标识常量(用于判断是否属于自己机器发送的消息)
	 */
	public static final String CLIENT_ID = UUID.randomUUID().toString();
	/**
	 * 消息包
	 */
	private ImPacket packet;
	/**
	 * 集群各个机器唯一标识(用于判断是否属于自己机器发送的消息)
	 */
	private String clientId = CLIENT_ID;
	/**
	 * 目标群组ID
	 */
	private String group;
	/**
	 * 目标用户ID
	 */
	private String userId;
	/**
	 * 目标token票据
	 */
	private String token;
	/**
	 * 目标IP
	 */
	private String ip;
	/**
	 * 目标通道ID
	 */
	private String channelId;
	/**
	 * 发送到集群所有通道
	 */
	private boolean toAll = false;

	/**
	 * 默认构造器
	 * @author: WChao
	 */
	public ImClusterVO() {}

	public ImClusterVO(ImPacket packet) {
		this.packet = packet;
	}

	public ImPacket getPacket() {
		return packet;
	}

	public void setPacket(ImPacket packet) {
		this.packet = packet;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isToAll() {
		return toAll;
	}

	public void setToAll(boolean toAll) {
		this.toAll = toAll;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
