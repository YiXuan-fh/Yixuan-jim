/**
 * 
 */
package org.jim.core.cluster;

import org.jim.core.ImPacket;

/**
 * @desc 集群接口
 * @author WChao
 * @date 2020-05-01 02:30
 */
public interface ICluster {
	/**
	 * 发送到其他机器的用户
	 * @param userId 用户绑定ID
	 * @param packet 消息包
	 */
	 void clusterToUser(String userId, ImPacket packet);
	/**
	 * 发送到其他机器的指定群组
	 * @param group 群组ID
	 * @param packet 消息包
	 */
	 void clusterToGroup(String group, ImPacket packet);
	/**
	 * 发送到其他机器上指定IP的channel
	 * @param ip 目标ip地址
	 * @param packet 消息包
	 */
	 void clusterToIp(String ip, ImPacket packet);
	/**
	 * 发送到其他机器上的指定通道
	 * @param channelId 通道ID
	 * @param packet 消息包
	 */
	 void clusterToChannelId(String channelId, ImPacket packet);
}
