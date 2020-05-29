package org.jim.server.demo.listener;

import org.jim.core.*;
import org.jim.core.exception.ImException;
import org.jim.core.packets.*;
import org.jim.core.utils.JsonKit;
import org.jim.server.JimServerAPI;
import org.jim.server.listener.AbstractImGroupListener;
import org.jim.server.protocol.ProtocolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 群组绑定监听器
 * @author WChao 
 * 2017年5月13日 下午10:38:36
 */
public class ImDemoGroupListener extends AbstractImGroupListener {

	private  static Logger logger = LoggerFactory.getLogger(ImDemoGroupListener.class);

	@Override
	public void doAfterBind(ImChannelContext imChannelContext, Group group) throws ImException {
		logger.info("群组:{},绑定成功!", JsonKit.toJSONString(group));
		JoinGroupRespBody joinGroupRespBody = JoinGroupRespBody.success();
		//回一条消息，告诉对方进群结果
		joinGroupRespBody.setGroup(group.getGroupId());
		ImPacket respPacket = ProtocolManager.Converter.respPacket(joinGroupRespBody, imChannelContext);
		//Jim.send(imChannelContext, respPacket);
		//发送进房间通知;
		joinGroupNotify(group, imChannelContext);
	}

	/**
	 * @param imChannelContext
	 * @param group
	 * @throws Exception
	 * @author: WChao
	 */
	@Override
	public void doAfterUnbind(ImChannelContext imChannelContext, Group group) throws ImException {
		//发退出房间通知  COMMAND_EXIT_GROUP_NOTIFY_RESP
		ExitGroupNotifyRespBody exitGroupNotifyRespBody = new ExitGroupNotifyRespBody();
		exitGroupNotifyRespBody.setGroup(group.getGroupId());
		User clientUser = imChannelContext.getSessionContext().getImClientNode().getUser();
		if(clientUser == null) {
			return;
		}
		User notifyUser = User.newBuilder().userId(clientUser.getUserId()).nick(clientUser.getNick()).build();
		exitGroupNotifyRespBody.setUser(notifyUser);

		RespBody respBody = new RespBody(Command.COMMAND_EXIT_GROUP_NOTIFY_RESP,exitGroupNotifyRespBody);
		ImPacket imPacket = new ImPacket(Command.COMMAND_EXIT_GROUP_NOTIFY_RESP, respBody.toByte());
		JimServerAPI.sendToGroup(group.getGroupId(), imPacket);
	}

	/**
	 * 发送进房间通知;
	 * @param group 群组对象
	 * @param imChannelContext
	 */
	public void joinGroupNotify(Group group, ImChannelContext imChannelContext)throws ImException{
		ImSessionContext imSessionContext = imChannelContext.getSessionContext();
		User clientUser = imSessionContext.getImClientNode().getUser();
		User notifyUser = User.newBuilder().userId(clientUser.getUserId()).nick(clientUser.getNick()).status(UserStatusType.ONLINE.getStatus()).build();
		String groupId = group.getGroupId();
		//发进房间通知  COMMAND_JOIN_GROUP_NOTIFY_RESP
		JoinGroupNotifyRespBody joinGroupNotifyRespBody = JoinGroupNotifyRespBody.success();
		joinGroupNotifyRespBody.setGroup(groupId).setUser(notifyUser);
		JimServerAPI.sendToGroup(groupId, ProtocolManager.Converter.respPacket(joinGroupNotifyRespBody,imChannelContext));
	}

}
