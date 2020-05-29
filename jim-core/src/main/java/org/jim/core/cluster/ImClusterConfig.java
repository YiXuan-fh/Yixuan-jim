package org.jim.core.cluster;

/**
 * @desc IM集群配置抽象类
 * @author WChao
 * 2018年05月20日 下午1:09:16
 */
public abstract class ImClusterConfig {
	
	/**
	 * 群组是否集群（同一个群组是否会分布在不同的机器上），false:不集群，默认不集群
	 */
	private boolean cluster2group = true;
	/**
	 * 用户是否集群（同一个用户是否会分布在不同的机器上），false:不集群，默认集群
	 */
	private boolean cluster2user = true;
	/**
	 * ip是否集群（同一个ip是否会分布在不同的机器上），false:不集群，默认集群
	 */
	private boolean cluster2ip = true;
	/**
	 * id是否集群（在A机器上的客户端是否可以通过channelId发消息给B机器上的客户端），false:不集群，默认集群<br>
	 */
	private boolean cluster2channelId = true;
	/**
	 * 所有连接是否集群（同一个ip是否会分布在不同的机器上），false:不集群，默认集群
	 */
	private boolean cluster2all = true;

	/**
	 * 同步发送
	 * @param imClusterVo 集群消息体
	 */
	public abstract void send(ImClusterVO imClusterVo);

	/**
	 * 异步发送
	 * @param imClusterVo 集群消息体
	 */
	public abstract void sendAsync(ImClusterVO imClusterVo);

	public boolean isCluster2group() {
		return cluster2group;
	}

	public void setCluster2group(boolean cluster2group) {
		this.cluster2group = cluster2group;
	}

	public boolean isCluster2user() {
		return cluster2user;
	}

	public void setCluster2user(boolean cluster2user) {
		this.cluster2user = cluster2user;
	}

	public boolean isCluster2ip() {
		return cluster2ip;
	}

	public void setCluster2ip(boolean cluster2ip) {
		this.cluster2ip = cluster2ip;
	}

	public boolean isCluster2channelId() {
		return cluster2channelId;
	}

	public void setCluster2channelId(boolean cluster2channelId) {
		this.cluster2channelId = cluster2channelId;
	}

	public boolean isCluster2all() {
		return cluster2all;
	}

	public void setCluster2all(boolean cluster2all) {
		this.cluster2all = cluster2all;
	}
}
