package org.jim.core;

import org.jim.core.packets.ImClientNode;
import org.tio.monitor.RateLimiterWrap;

/**
 *
 * @desc IM会话上下文信息
 * @date 2018-05-01
 * @author wchao 
 *
 */
public class ImSessionContext {
	/**
	 * 消息请求频率控制器
	 */
	protected RateLimiterWrap requestRateLimiter = null;
	/**
	 * 客户端Node信息
	 */
	protected ImClientNode imClientNode = null;
	/**
	 * 客户端票据token,一般业务设置用
	 */
	protected String token = null;
	/**
	 * 所属通道上下文
	 */
	protected ImChannelContext imChannelContext;
	/**
	 * sessionContext唯一ID
	 */
	protected String id;

	/**
	 * @author: WChao
	 * 2017年2月21日 上午10:27:54
	 */
	public ImSessionContext(){}

	public ImSessionContext(ImChannelContext imChannelContext){
		this.imChannelContext = imChannelContext;
	}

	public ImClientNode getImClientNode() {
		return imClientNode;
	}

	public void setImClientNode(ImClientNode imClientNode) {
		this.imClientNode = imClientNode;
	}

	/**
	 * @return the token
	 */
	public String getToken()
	{
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token)
	{
		this.token = token;
	}

	/**
	 * @return the requestRateLimiter
	 */
	public RateLimiterWrap getRequestRateLimiter() {
		return requestRateLimiter;
	}

	/**
	 * @param requestRateLimiter the requestRateLimiter to set
	 */
	public void setRequestRateLimiter(RateLimiterWrap requestRateLimiter) {
		this.requestRateLimiter = requestRateLimiter;
	}

	public ImChannelContext getImChannelContext() {
		return imChannelContext;
	}

	public void setImChannelContext(ImChannelContext imChannelContext) {
		this.imChannelContext = imChannelContext;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
