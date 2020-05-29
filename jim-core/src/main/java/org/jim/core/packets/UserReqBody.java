/**
 * 
 */
package org.jim.core.packets;

/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年9月26日 上午11:44:10
 */
public class UserReqBody extends Message{
	
	private static final long serialVersionUID = 1861307516710578262L;
	/**
	 * 用户id;
	 */
	private String userId;
	/**
	 * 0:单个,1:所有在线用户,2:所有用户(在线+离线);
	 */
	private Integer type;
	/**
	 * 好友分组id;
	 */
	private String friendGroupId;
	/**
	 * 群组id;
	 */
	private String groupId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getFriendGroupId() {
		return friendGroupId;
	}

	public void setFriendGroupId(String friendGroupId) {
		this.friendGroupId = friendGroupId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
