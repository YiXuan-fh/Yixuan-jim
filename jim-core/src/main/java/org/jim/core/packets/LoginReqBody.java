/**
 * 
 */
package org.jim.core.packets;

/**
 * 版本: [1.0]
 * 功能说明: 登陆命令请求包体
 * 作者: WChao 创建时间: 2017年9月12日 下午3:13:22
 */
public class LoginReqBody extends Message {
	
	private static final long serialVersionUID = -10113316720288444L;
	/**
	 * 用户Id
	 */
	private String userId;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 登陆token
	 */
	private String token;
	
	public LoginReqBody(){}
	
	public LoginReqBody(String token){
		this.token = token;
		this.cmd = Command.COMMAND_LOGIN_REQ.getNumber();
	}

	public LoginReqBody(String userId,String password){
		this.userId = userId;
		this.password = password;
		this.cmd = Command.COMMAND_LOGIN_REQ.getNumber();
	}

	public LoginReqBody(String userId,String password,String token){
		this(userId,password);
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
