/**
 * 
 */
package org.jim.core.ws;

import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.ImSessionContext;
import org.jim.core.packets.Command;
import org.jim.core.protocol.IProtocolConverter;

/**
 * Ws协议消息转化包
 * @author WChao
 *
 */
public class WsConvertPacket implements IProtocolConverter {

	/**
	 * WebSocket响应包;
	 */
	@Override
	public ImPacket RespPacket(byte[] body, Command command, ImChannelContext channelContext) {
		ImSessionContext sessionContext = channelContext.getSessionContext();
		//转ws协议响应包;
		if(sessionContext instanceof WsSessionContext){
			WsResponsePacket wsResponsePacket = new WsResponsePacket();
			wsResponsePacket.setBody(body);
			wsResponsePacket.setWsOpcode(Opcode.TEXT);
			wsResponsePacket.setCommand(command);
			return wsResponsePacket;
		}
		return null;
	}

	@Override
	public ImPacket RespPacket(ImPacket imPacket, Command command, ImChannelContext imChannelContext) {

		return this.RespPacket(imPacket.getBody(), command, imChannelContext);
	}

	@Override
	public ImPacket ReqPacket(byte[] body, Command command, ImChannelContext channelContext) {
		
		return null;
	}
}
