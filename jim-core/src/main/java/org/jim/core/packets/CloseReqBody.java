package org.jim.core.packets;

/**
 * 退出指定用户请求体
 * @author WChao
 * @date 2018年4月13日 下午4:20:40
 */
public class CloseReqBody extends Message {

	private static final long serialVersionUID = 771895783302296339L;

	public CloseReqBody(){};

	public CloseReqBody(String userId){
		this.userId = userId;
	}

	/**
	 * 用户ID
	 */
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
