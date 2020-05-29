package org.jim.core.ws;

import java.util.List;

import org.jim.core.ImChannelContext;
import org.jim.core.ImSessionContext;
import org.jim.core.http.HttpRequest;
import org.jim.core.http.HttpResponse;

/**
 *
 * @author WChao
 *
 */
public class WsSessionContext extends ImSessionContext {
	/**
	 * 是否已经握过手
	 */
	private boolean isHandshaked = false;

	/**
	 * webSocket 握手请求包
	 */
	private HttpRequest handshakeRequestPacket = null;

	/**
	 * webSocket 握手响应包
	 */
	private HttpResponse handshakeResponsePacket = null;
	/**
	 * ws请求状态包;
	 */
	private WsRequestPacket wsRequestPacket = null;
	/**
	 * ws响应状态包;
	 */
	private WsResponsePacket wsResponsePacket = null;

	/**
	 * webSocket 协议用到的，有时候数据包是分几个到的，注意那个fin字段，本im暂时不支持
	 */
	private List<byte[]> lastParts = null;

	/**
	 *
	 * @author wchao
	 * 2017年2月21日 上午10:27:54
	 *
	 */
	public WsSessionContext(ImChannelContext imChannelContext) {
		super(imChannelContext);
	}

	/**
	 * @return the httpHandshakePacket
	 */
	public HttpRequest getHandshakeRequestPacket() {
		return handshakeRequestPacket;
	}

	/**
	 * @return the handshakeResponsePacket
	 */
	public HttpResponse getHandshakeResponsePacket() {
		return handshakeResponsePacket;
	}

	/**
	 * @return the lastPart
	 */
	public List<byte[]> getLastParts() {
		return lastParts;
	}

	/**
	 * @return the isHandshaked
	 */
	public boolean isHandshaked() {
		return isHandshaked;
	}

	/**
	 * @param isHandshaked the isHandshaked to set
	 */
	public void setHandshaked(boolean isHandshaked) {
		this.isHandshaked = isHandshaked;
	}

	/**
	 * @param  handshakeRequestPacket the httpHandshakePacket to set
	 */
	public void setHandshakeRequestPacket(HttpRequest handshakeRequestPacket) {
		this.handshakeRequestPacket = handshakeRequestPacket;
	}

	/**
	 * @param handshakeResponsePacket the handshakeResponsePacket to set
	 */
	public void setHandshakeResponsePacket(HttpResponse handshakeResponsePacket) {
		this.handshakeResponsePacket = handshakeResponsePacket;
	}

	/**
	 * @param lastParts the lastPart to set
	 */
	public void setLastParts(List<byte[]> lastParts) {
		this.lastParts = lastParts;
	}

	public WsRequestPacket getWsRequestPacket() {
		return wsRequestPacket;
	}

	public void setWsRequestPacket(WsRequestPacket wsRequestPacket) {
		this.wsRequestPacket = wsRequestPacket;
	}

	public WsResponsePacket getWsResponsePacket() {
		return wsResponsePacket;
	}

	public void setWsResponsePacket(WsResponsePacket wsResponsePacket) {
		this.wsResponsePacket = wsResponsePacket;
	}
}
