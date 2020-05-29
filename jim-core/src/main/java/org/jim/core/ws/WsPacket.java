package org.jim.core.ws;

import org.jim.core.ImPacket;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年9月6日 上午11:11:09
 */
public class WsPacket extends ImPacket {

	private static final long serialVersionUID = 4506947563506841436L;
	/**
	 * 消息体最多为多少
	 */
	public static final int MAX_LENGTH_OF_BODY = (int) (1024 * 1024 * 2.1); //只支持多少M数据
	public static final int MINIMUM_HEADER_LENGTH = 2;

	public static final int MAX_BODY_LENGTH = 1024 * 512; //最多接受的1024 * 512(半M)数据

	public static final String CHARSET_NAME = "utf-8";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/**
	 * 是否是握手包
	 */
	private boolean isHandShake = false;

	private boolean wsEof;

	private Opcode wsOpcode = Opcode.BINARY;

	private boolean wsHasMask;

	private long wsBodyLength;

	private byte[] wsMask;

	private String wsBodyText; //当为文本时才有此字段

	public WsPacket() {

	}

	public WsPacket(byte[] body) {
		this();
		this.body = body;
	}

	/**
	 * @return the wsBodyLength
	 */
	public long getWsBodyLength() {
		return wsBodyLength;
	}

	/**
	 * @return the wsBodyText
	 */
	public String getWsBodyText() {
		return wsBodyText;
	}

	/**
	 * @return the wsMask
	 */
	public byte[] getWsMask() {
		return wsMask;
	}

	/**
	 * @return the wsOpcode
	 */
	public Opcode getWsOpcode() {
		return wsOpcode;
	}

	/**
	 * @return the isHandShake
	 */
	public boolean isHandShake() {
		return isHandShake;
	}

	/**
	 * @return the wsEof
	 */
	public boolean isWsEof() {
		return wsEof;
	}

	/**
	 * @return the wsHasMask
	 */
	public boolean isWsHasMask() {
		return wsHasMask;
	}

	/**
	 * @param isHandShake the isHandShake to set
	 */
	public void setHandShake(boolean isHandShake) {
		this.isHandShake = isHandShake;
	}

	/**
	 * @param wsBodyLength the wsBodyLength to set
	 */
	public void setWsBodyLength(long wsBodyLength) {
		this.wsBodyLength = wsBodyLength;
	}

	/**
	 * @param wsBodyText the wsBodyText to set
	 */
	public void setWsBodyText(String wsBodyText) {
		this.wsBodyText = wsBodyText;
	}

	/**
	 * @param wsEof the wsEof to set
	 */
	public void setWsEof(boolean wsEof) {
		this.wsEof = wsEof;
	}

	/**
	 * @param wsHasMask the wsHasMask to set
	 */
	public void setWsHasMask(boolean wsHasMask) {
		this.wsHasMask = wsHasMask;
	}

	/**
	 * @param wsMask the wsMask to set
	 */
	public void setWsMask(byte[] wsMask) {
		this.wsMask = wsMask;
	}

	/**
	 * @param wsOpcode the wsOpcode to set
	 */
	public void setWsOpcode(Opcode wsOpcode) {
		this.wsOpcode = wsOpcode;
	}
}
