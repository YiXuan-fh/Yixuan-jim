/**
 * 
 */
package org.jim.core.packets;

/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年9月12日 下午2:49:49
 */
public class AuthReqBody extends Message {
	
	private static final long serialVersionUID = -5687459633884615894L;
	private String token;//token验证;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
