package org.jim.core.packets;

/**
 * @author WChao
 * @date 2018年4月10日 下午3:18:06
 */
public class MessageReqBody extends Message {

	private static final long serialVersionUID = -4748178964168947701L;
	/**
	 * 发送用户id;
	 */
	private String fromUserId;
	/**
	 * 接收用户id;
	 */
	private String userId;
	/**
	 * 群组id;
	 */
	private String groupId;
	/**
	 * 0:离线消息,1:历史消息;
	 */
	private Integer type;
	/**
	 * 消息开始时间;
	 */
	private Double beginTime;
	/**
	 * 消息结束时间
	 */
	private Double endTime;
	/**
	 * 分页偏移量
	 */
	private Integer offset;
	/**
	 * 数量
	 */
	private Integer count;
	
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Double getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Double beginTime) {
		this.beginTime = beginTime;
	}
	public Double getEndTime() {
		return endTime;
	}
	public void setEndTime(Double endTime) {
		this.endTime = endTime;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
}
