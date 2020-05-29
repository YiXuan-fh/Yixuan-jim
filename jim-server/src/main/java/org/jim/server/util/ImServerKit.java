/**
 * 
 */
package org.jim.server.util;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.jim.core.ImChannelContext;
import org.jim.core.packets.User;
import org.jim.server.JimServerAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * IM工具类;
 * @author WChao
 *
 */
public class ImServerKit {
	
	private static Logger logger = LoggerFactory.getLogger(ImServerKit.class);

	/**
	 * 根据群组获取所有用户;
	 * @param groupId 群组ID
	 * @return 群组用户集合列表
	 */
	public static List<User> getUsersByGroup(String groupId){
		List<ImChannelContext> channelContexts = JimServerAPI.getByGroup(groupId);
		List<User> users = Lists.newArrayList();
		if(CollectionUtils.isEmpty(channelContexts)){
			return users;
		}
		Map<String,User> userMap = new HashMap<>();
		channelContexts.forEach(imChannelContext -> {
			User user = imChannelContext.getSessionContext().getImClientNode().getUser();
			if(Objects.nonNull(user) && Objects.isNull(userMap.get(user.getUserId()))){
				userMap.put(user.getUserId(), user);
				users.add(user);
			}
		});
		return users;
	}

	/**
	 * 根据用户ID获取用户信息(一个用户ID会有多端通道,默认取第一个)
	 * @param userId 用户ID
	 * @return user信息
	 */
	public static User getUser(String userId){
		List<ImChannelContext> imChannelContexts = JimServerAPI.getByUserId(userId);
		if(CollectionUtils.isEmpty(imChannelContexts)) {
			return null;
		}
		return imChannelContexts.get(0).getSessionContext().getImClientNode().getUser();
	}

}
