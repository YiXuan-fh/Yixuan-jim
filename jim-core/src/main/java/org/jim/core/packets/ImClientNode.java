/**
 * 
 */
package org.jim.core.packets;

/**
 * 版本: [1.0]
 * 功能说明: 客户端信息类
 * 作者: WChao 创建时间: 2017年7月26日 下午3:11:55
 */
public class ImClientNode extends Message{
	
	private static final long serialVersionUID = 6196600593975727155L;
	/**
	 * 客户端ip
	 */
	private String ip;
	/**
	 * 客户端远程port
	 */
	private int port;
	/**
	 * 如果没登录过，则为null
	 */
	private User user;
	/**
	 * 地区
	 */
	private String region;
	/**
	 * 浏览器信息(这里暂时放在这,后面会扩展出比如httpImClientNode、TcpImClientNode等)
	 */
	private String useragent;

	private ImClientNode(){}

	private ImClientNode(String id, String ip, int port, User user, String region, String useragent){
		this.id = id;
		this.ip = ip;
		this.port = port;
		this.user = user;
		this.region = region;
		this.useragent = useragent;
	}

	public static ImClientNode.Builder newBuilder(){
		return new ImClientNode.Builder();
	}

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getUseragent() {
		return useragent;
	}
	public void setUseragent(String useragent) {
		this.useragent = useragent;
	}

	public static class Builder extends Message.Builder<ImClientNode,ImClientNode.Builder> {
		/**
		 * 客户端ip
		 */
		private String ip;
		/**
		 * 客户端远程port
		 */
		private int port;
		/**
		 * 如果没登录过，则为null
		 */
		private User user;
		/**
		 * 地区
		 */
		private String region;
		/**
		 * 浏览器信息
		 */
		private String useragent;

		public Builder() {}

		public ImClientNode.Builder id(String id) {
			this.id = id;
			return this;
		}

		public ImClientNode.Builder ip(String ip) {
			this.ip = ip;
			return this;
		}

		public ImClientNode.Builder port(int port) {
			this.port = port;
			return this;
		}

		public ImClientNode.Builder user(User user) {
			this.user = user;
			return this;
		}

		public ImClientNode.Builder region(String region) {
			this.region = region;
			return this;
		}

		public ImClientNode.Builder useragent(String useragent) {
			this.useragent = useragent;
			return this;
		}

		@Override
		protected ImClientNode.Builder getThis() {
			return this;
		}

		@Override
		public ImClientNode build() {
			return new ImClientNode(id, ip, port, user, region, useragent);
		}
	}

}
