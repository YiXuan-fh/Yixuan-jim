package org.jim.core.message;

import org.jim.core.ImChannelContext;
import org.jim.core.listener.ImStoreBindListener;
import org.jim.core.packets.ChatBody;
import org.jim.core.packets.Group;
import org.jim.core.packets.User;
import org.jim.core.packets.UserMessageData;

import java.util.List;
/**
 * @author WChao
 * @date 2018年4月9日 下午4:31:51
 */
public interface MessageHelper {
	/**
	 * 获取IM开启持久化时绑定/解绑群组、用户监听器;
	 * @return
	 */
	 ImStoreBindListener getBindListener();
	/**
	 * 判断用户是否在线
	 * @param userId 用户ID
	 * @return
	 */
	 boolean isOnline(String userId);
	/**
	 * 获取指定群组所有成员信息
	 * @param groupId 群组ID
	 * @param type(0:所有在线用户,1:所有离线用户,2:所有用户[在线+离线])
	 * @return
	 */
	 Group getGroupUsers(String groupId, Integer type);
	/**
	 * 获取用户所有群组成员信息
	 * @param userId 用户ID
	 * @param type(0:所有在线用户,1:所有离线用户,2:所有用户[在线+离线])
	 * @return
	 */
	 List<Group> getAllGroupUsers(String userId, Integer type);
	/**
	 * 获取好友分组所有成员信息
	 * @param userId 用户ID
	 * @param friendGroupId 好友分组ID
	 * @param type(0:所有在线用户,1:所有离线用户,2:所有用户[在线+离线])
	 * @return
	 */
	 Group getFriendUsers(String userId, String friendGroupId, Integer type);
	/**
	 * 获取好友分组所有成员信息
	 * @param userId 用户ID
	 * @param type(0:所有在线用户,1:所有离线用户,2:所有用户[在线+离线])
	 * @return
	 */
	 List<Group> getAllFriendUsers(String userId, Integer type);
	/**
	 * 根据在线类型获取用户信息;
	 * @param userId 用户ID
	 * @param type(0:所有在线用户,1:所有离线用户,2:所有用户[在线+离线])
	 * @return
	 */
	 User getUserByType(String userId, Integer type);
	/**
	 * 添加群组成员
	 * @param userId 用户ID
	 * @param groupId 群组ID
	 */
	 void addGroupUser(String userId, String groupId);
	/**
	 * 获取群组所有成员;
	 * @param groupId 群组ID
	 * @return
	 */
	 List<String> getGroupUsers(String groupId);
	/**
	 * 获取用户拥有的群组ID;
	 * @param userId 用户ID
	 * @return
	 */
	 List<String> getGroups(String userId);
	/**
	 * 消息持久化写入
	 * @param timelineTable 持久化表
	 * @param timelineId 持久化ID
	 * @param chatBody 消息记录
	 */
	 void writeMessage(String timelineTable, String timelineId , ChatBody chatBody);
	/**
	 * 移除群组用户
	 * @param userId 用户ID
	 * @param groupId 群组ID
	 */
	 void removeGroupUser(String userId, String groupId);
	/**
	 * 获取与指定用户离线消息;
	 * @param userId 用户ID
	 * @param fromUserId 目标用户ID
	 * @return
	 */
	 UserMessageData getFriendsOfflineMessage(String userId, String fromUserId);
	/**
	 * 获取与所有用户离线消息;
	 * @param userId 用户ID
	 * @return
	 */
	 UserMessageData getFriendsOfflineMessage(String userId);
	/**
	 * 获取用户指定群组离线消息;
	 * @param userId 用户ID
	 * @param groupId 群组ID
	 * @return
	 */
	 UserMessageData getGroupOfflineMessage(String userId,String groupId);
	/**
	 * 获取与指定用户历史消息;
	 * @param userId 用户ID
	 * @param fromUserId 目标用户ID
	 * @param beginTime 消息区间开始时间
	 * @param endTime 消息区间结束时间
	 * @param offset 分页偏移量
	 * @param count 数量
	 * @return
	 */
	 UserMessageData getFriendHistoryMessage(String userId, String fromUserId, Double beginTime, Double endTime, Integer offset, Integer count);
	
	/**
	 * 获取与指定群组历史消息;
	 * @param userId 用户ID
	 * @param groupId 群组ID
	 * @param beginTime 消息区间开始时间
	 * @param endTime 消息区间结束时间
	 * @param offset 分页偏移量
	 * @param count 数量
	 * @return
	 */
	 UserMessageData getGroupHistoryMessage(String userId, String groupId, Double beginTime, Double endTime, Integer offset, Integer count);

	/**
	 * 更新用户终端协议类型及在线状态;(在线:online:离线:offline)
	 * @param user 用户信息
	 */
	 boolean updateUserTerminal(User user);


}
