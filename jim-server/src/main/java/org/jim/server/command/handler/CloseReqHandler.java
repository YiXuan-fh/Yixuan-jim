package org.jim.server.command.handler;

import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.ImStatus;
import org.jim.core.exception.ImException;
import org.jim.core.packets.CloseReqBody;
import org.jim.core.packets.Command;
import org.jim.core.packets.RespBody;
import org.jim.core.utils.JsonKit;
import org.jim.server.JimServerAPI;
import org.jim.server.command.AbstractCmdHandler;
import org.jim.server.protocol.ProtocolManager;

/**
 * 版本: [1.0]
 * 功能说明: 关闭请求cmd命令处理器
 * @author : WChao 创建时间: 2017年9月21日 下午3:33:23
 */
public class CloseReqHandler extends AbstractCmdHandler
{
	@Override
	public ImPacket handler(ImPacket packet, ImChannelContext imChannelContext) throws ImException
	{
		CloseReqBody closeReqBody;
		try{
			closeReqBody = JsonKit.toBean(packet.getBody(),CloseReqBody.class);
		}catch (Exception e) {
			//关闭请求消息格式不正确
			return ProtocolManager.Converter.respPacket(new RespBody(Command.COMMAND_CLOSE_REQ, ImStatus.C10020), imChannelContext);
		}
		JimServerAPI.bSend(imChannelContext, ProtocolManager.Converter.respPacket(new RespBody(Command.COMMAND_CLOSE_REQ, ImStatus.C10021), imChannelContext));
		if(closeReqBody == null || closeReqBody.getUserId() == null){
			JimServerAPI.remove(imChannelContext, "收到关闭请求");
		}else{
			String userId = closeReqBody.getUserId();
			JimServerAPI.remove(userId, "收到关闭请求!");
		}
		return null;
	}

	@Override
	public Command command() {
		return Command.COMMAND_CLOSE_REQ;
	}
}
