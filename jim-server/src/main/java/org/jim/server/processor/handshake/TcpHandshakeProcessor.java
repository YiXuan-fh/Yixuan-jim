/**
 * 
 */
package org.jim.server.processor.handshake;

import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.ImSessionContext;
import org.jim.core.exception.ImException;
import org.jim.core.packets.Command;
import org.jim.core.packets.HandshakeBody;
import org.jim.core.packets.RespBody;
import org.jim.core.tcp.TcpSessionContext;
import org.jim.server.protocol.AbstractProtocolCmdProcessor;
import org.jim.server.protocol.ProtocolManager;

/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年9月11日 下午8:11:34
 */
public class TcpHandshakeProcessor extends AbstractProtocolCmdProcessor implements HandshakeCmdProcessor{

	@Override
	public ImPacket handshake(ImPacket packet, ImChannelContext channelContext) throws ImException {
		RespBody handshakeBody = new RespBody(Command.COMMAND_HANDSHAKE_RESP,new HandshakeBody(Protocol.HANDSHAKE_BYTE));
		ImPacket handshakePacket = ProtocolManager.Converter.respPacket(handshakeBody,channelContext);
		return handshakePacket;
	}

	/**
	 * 握手成功后
	 * @param packet
	 * @param channelContext
	 * @throws ImException
	 * @author Wchao
	 */
	@Override
	public void onAfterHandshake(ImPacket packet, ImChannelContext channelContext)throws ImException {
		
	}

	@Override
	public boolean isProtocol(ImChannelContext channelContext){
		ImSessionContext sessionContext = channelContext.getSessionContext();
		if(sessionContext == null){
			return false;
		}else if(sessionContext instanceof TcpSessionContext){
			return true;
		}
		return false;
	}
	
}
