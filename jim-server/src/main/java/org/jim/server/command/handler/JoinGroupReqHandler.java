package org.jim.server.command.handler;

import org.apache.commons.lang3.StringUtils;
import org.jim.core.*;
import org.jim.core.exception.ImException;
import org.jim.server.JimServerAPI;
import org.jim.server.processor.group.GroupCmdProcessor;
import org.jim.server.protocol.ProtocolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jim.core.packets.Command;
import org.jim.core.packets.Group;
import org.jim.core.packets.JoinGroupRespBody;
import org.jim.core.packets.JoinGroupResult;
import org.jim.core.utils.JsonKit;
import org.jim.server.command.AbstractCmdHandler;
import java.util.Objects;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 加入群组消息cmd命令处理器
 * @author : WChao 创建时间: 2017年9月21日 下午3:33:23
 */
public class JoinGroupReqHandler extends AbstractCmdHandler {
	
	private static Logger log = LoggerFactory.getLogger(JoinGroupReqHandler.class);
	
	@Override
	public ImPacket handler(ImPacket packet, ImChannelContext imChannelContext) throws ImException {
		//绑定群组;
		Group joinGroup = JsonKit.toBean(packet.getBody(),Group.class);
		String groupId = joinGroup.getGroupId();
		if (StringUtils.isBlank(groupId)) {
			log.error("group is null,{}", imChannelContext);
			JimServerAPI.close(imChannelContext, "group is null when join group");
			return null;
		}
		//实际绑定之前执行处理器动作
		GroupCmdProcessor groupProcessor = this.getSingleProcessor(GroupCmdProcessor.class);
		//当有群组处理器时候才会去处理
		if(Objects.nonNull(groupProcessor)){
			JoinGroupRespBody joinGroupRespBody = groupProcessor.join(joinGroup, imChannelContext);
			boolean joinGroupIsTrue = Objects.isNull(joinGroupRespBody) || JoinGroupResult.JOIN_GROUP_RESULT_OK.getNumber() != joinGroupRespBody.getResult().getNumber();
			if (joinGroupIsTrue) {
				joinGroupRespBody = JoinGroupRespBody.failed().setData(joinGroupRespBody);
				ImPacket respPacket = ProtocolManager.Converter.respPacket(joinGroupRespBody, imChannelContext);
				return respPacket;
			}
		}
		//处理完处理器内容后
		JimServerAPI.bindGroup(imChannelContext, joinGroup);
		return null;
	}
	@Override
	public Command command() {
		
		return Command.COMMAND_JOIN_GROUP_REQ;
	}
}
