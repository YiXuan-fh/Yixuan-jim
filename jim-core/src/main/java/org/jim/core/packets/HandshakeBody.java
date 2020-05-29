/**
 * 
 */
package org.jim.core.packets;

/**
 * @author WChao
 *
 */
public class HandshakeBody extends Message{

	private static final long serialVersionUID = 4493254915372077140L;
	private byte hbyte;
	
	public HandshakeBody(){}
	public HandshakeBody(byte hbyte){
		this.hbyte = hbyte;
	}
	public byte getHbyte() {
		return hbyte;
	}

	public HandshakeBody setHbyte(byte hbyte) {
		this.hbyte = hbyte;
		return this;
	}
	
}
