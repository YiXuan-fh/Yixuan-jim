/**
 * 
 */
package org.jim.core.tcp;

import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.ImSessionContext;
import org.jim.core.packets.Command;
import org.jim.core.protocol.IProtocolConverter;

/**
 * TCP协议消息转化包
 * @author WChao
 *
 */
public class TcpConvertPacket implements IProtocolConverter {

	/**
	 * 转TCP协议响应包;
	 */
	@Override
	public ImPacket RespPacket(byte[] body, Command command, ImChannelContext imChannelContext) {
		ImSessionContext sessionContext = imChannelContext.getSessionContext();
		if(sessionContext instanceof TcpSessionContext){
			TcpPacket tcpPacket = new TcpPacket(command,body);
			TcpServerEncoder.encode(tcpPacket, imChannelContext.getImConfig(), imChannelContext);
			tcpPacket.setCommand(command);
			return tcpPacket;
		}
		return null;
	}

	@Override
	public ImPacket RespPacket(ImPacket imPacket, Command command, ImChannelContext imChannelContext) {

		return this.RespPacket(imPacket.getBody(), command, imChannelContext);
	}

	/**
	 * 转TCP协议请求包;
	 */
	@Override
	public ImPacket ReqPacket(byte[] body, Command command, ImChannelContext channelContext) {
		Object sessionContext = channelContext.getSessionContext();
		if(sessionContext instanceof TcpSessionContext){
			TcpPacket tcpPacket = new TcpPacket(command,body);
			TcpServerEncoder.encode(tcpPacket, channelContext.getImConfig(), channelContext);
			tcpPacket.setCommand(command);
			return tcpPacket;
		}
		return null;
	}

}
