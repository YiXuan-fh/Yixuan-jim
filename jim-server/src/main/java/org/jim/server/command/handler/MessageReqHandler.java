package org.jim.server.command.handler;

import org.apache.commons.lang3.StringUtils;
import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.ImStatus;
import org.jim.core.config.ImConfig;
import org.jim.core.exception.ImException;
import org.jim.server.config.ImServerConfig;
import org.jim.core.message.MessageHelper;
import org.jim.core.packets.Command;
import org.jim.core.packets.RespBody;
import org.jim.core.packets.UserMessageData;
import org.jim.core.packets.MessageReqBody;
import org.jim.core.utils.JsonKit;
import org.jim.server.command.AbstractCmdHandler;
import org.jim.server.protocol.ProtocolManager;

/**
 * 获取聊天消息命令处理器
 * @author WChao
 * @date 2018年4月10日 下午2:40:07
 */
public class MessageReqHandler extends AbstractCmdHandler {
	
	@Override
	public Command command() {
		
		return Command.COMMAND_GET_MESSAGE_REQ;
	}

	@Override
	public ImPacket handler(ImPacket packet, ImChannelContext imChannelContext) throws ImException {
		RespBody resPacket;
		MessageReqBody messageReqBody;
		try{
			messageReqBody = JsonKit.toBean(packet.getBody(),MessageReqBody.class);
		}catch (Exception e) {
			//用户消息格式不正确
			return getMessageFailedPacket(imChannelContext);
		}
		UserMessageData messageData = null;
		ImServerConfig imServerConfig = ImConfig.Global.get();
		MessageHelper messageHelper = imServerConfig.getMessageHelper();
		//群组ID;
		String groupId = messageReqBody.getGroupId();
		//当前用户ID;
		String userId = messageReqBody.getUserId();
		//消息来源用户ID;
		String fromUserId = messageReqBody.getFromUserId();
		//消息区间开始时间;
		Double beginTime = messageReqBody.getBeginTime();
		//消息区间结束时间;
		Double endTime = messageReqBody.getEndTime();
		//分页偏移量;
		Integer offset = messageReqBody.getOffset();
		//分页数量;
		Integer count = messageReqBody.getCount();
		//消息类型;
		int type = messageReqBody.getType();
		//如果用户ID为空或者type格式不正确，获取消息失败;
		if(StringUtils.isEmpty(userId) || (0 != type && 1 != type) || !ImServerConfig.ON.equals(imServerConfig.getIsStore())){
			return getMessageFailedPacket(imChannelContext);
		}
		if(type == 0){
			resPacket = new RespBody(Command.COMMAND_GET_MESSAGE_RESP,ImStatus.C10016);
		}else{
			resPacket = new RespBody(Command.COMMAND_GET_MESSAGE_RESP,ImStatus.C10018);
		}
		//群组ID不为空获取用户该群组消息;
		if(!StringUtils.isEmpty(groupId)){
			//离线消息;
			if(0 == type){
				messageData = messageHelper.getGroupOfflineMessage(userId,groupId);
			//历史消息;
			}else if(1 == type){
				messageData = messageHelper.getGroupHistoryMessage(userId, groupId,beginTime,endTime,offset,count);
			}
		}else if(StringUtils.isEmpty(fromUserId)){
			//获取用户所有离线消息(好友+群组);
			if(0 == type){
				messageData = messageHelper.getFriendsOfflineMessage(userId);
			}else{
				return getMessageFailedPacket(imChannelContext);
			}
		}else{
			//获取与指定用户离线消息;
			if(0 == type){
				messageData = messageHelper.getFriendsOfflineMessage(userId, fromUserId);
			//获取与指定用户历史消息;
			}else if(1 == type){
				messageData = messageHelper.getFriendHistoryMessage(userId, fromUserId,beginTime,endTime,offset,count);
			}
		}
		resPacket.setData(messageData);
		return ProtocolManager.Converter.respPacket(resPacket, imChannelContext);
	}
	/**
	 * 获取用户消息失败响应包;
	 * @param imChannelContext
	 * @return
	 */
	public ImPacket getMessageFailedPacket(ImChannelContext imChannelContext) throws ImException{
		RespBody resPacket = new RespBody(Command.COMMAND_GET_MESSAGE_RESP,ImStatus.C10015);
		return ProtocolManager.Converter.respPacket(resPacket, imChannelContext);
	}
}
