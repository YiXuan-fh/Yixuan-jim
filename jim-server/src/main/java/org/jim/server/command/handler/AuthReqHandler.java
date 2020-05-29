package org.jim.server.command.handler;

import org.jim.core.ImChannelContext;
import org.jim.core.ImConst;
import org.jim.core.ImPacket;
import org.jim.core.ImStatus;
import org.jim.core.exception.ImException;
import org.jim.core.packets.AuthReqBody;
import org.jim.core.packets.Command;
import org.jim.core.packets.RespBody;
import org.jim.core.utils.JsonKit;
import org.jim.server.command.AbstractCmdHandler;
import org.jim.server.protocol.ProtocolManager;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 鉴权请求消息命令处理器
 * @author : WChao 创建时间: 2017年9月13日 下午1:39:35
 */
public class AuthReqHandler extends AbstractCmdHandler
{
	@Override
	public ImPacket handler(ImPacket packet, ImChannelContext channelContext) throws ImException
	{
		if (packet.getBody() == null) {
			RespBody respBody = new RespBody(Command.COMMAND_AUTH_RESP,ImStatus.C10010);
			return ProtocolManager.Converter.respPacket(respBody, channelContext);
		}
		AuthReqBody authReqBody = JsonKit.toBean(packet.getBody(), AuthReqBody.class);
		String token = authReqBody.getToken() == null ? "" : authReqBody.getToken();
		String data = token +  ImConst.AUTH_KEY;
		authReqBody.setToken(data);
		authReqBody.setCmd(null);
		RespBody respBody = new RespBody(Command.COMMAND_AUTH_RESP,ImStatus.C10009).setData(authReqBody);
		return ProtocolManager.Converter.respPacket(respBody, channelContext);
	}

	@Override
	public Command command() {
		return Command.COMMAND_AUTH_REQ;
	}
}
