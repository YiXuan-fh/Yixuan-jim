package org.jim.server.processor.chat;

import org.jim.core.ImChannelContext;
import org.jim.core.config.ImConfig;
import org.jim.core.message.MessageHelper;
import org.jim.core.packets.ChatBody;
import org.jim.core.packets.ChatType;
import org.jim.core.packets.Message;
import org.jim.server.config.ImServerConfig;
import org.jim.server.processor.SingleProtocolCmdProcessor;
import org.jim.server.util.ChatKit;

import java.util.List;
/**
 * @author WChao
 * @date 2018年4月3日 下午1:13:32
 */
public abstract class BaseAsyncChatMessageProcessor implements SingleProtocolCmdProcessor {
	
	protected ImServerConfig imServerConfig = ImConfig.Global.get();
	/**
	 * 供子类拿到消息进行业务处理(如:消息持久化到数据库等)的抽象方法
	 * @param chatBody
	 * @param imChannelContext
	 */
    protected abstract void doProcess(ChatBody chatBody, ImChannelContext imChannelContext);

	@Override
	public void process(ImChannelContext imChannelContext, Message message) {
		ChatBody chatBody = (ChatBody)message;
		//开启持久化
		boolean isStore = ImServerConfig.ON.equals(imServerConfig.getIsStore());
		if(isStore){
			//存储群聊消息;
			if(ChatType.CHAT_TYPE_PUBLIC.getNumber() == chatBody.getChatType()){
				pushGroupMessages(PUSH, STORE, chatBody, isStore);
			}else{
				String from = chatBody.getFrom();
				String to = chatBody.getTo();
				String sessionId = ChatKit.sessionId(from,to);
				writeMessage(STORE,USER+":"+sessionId,chatBody);
				boolean isOnline = ChatKit.isOnline(to, isStore);
				if(!isOnline){
					writeMessage(PUSH,USER+":"+to+":"+from,chatBody);
				}
			}
		}
		doProcess(chatBody, imChannelContext);
	}

	/**
	 * 推送持久化群组消息
	 * @param pushTable
	 * @param storeTable
	 * @param chatBody
	 */
	private void pushGroupMessages(String pushTable, String storeTable , ChatBody chatBody, boolean isStore){
		MessageHelper messageHelper = imServerConfig.getMessageHelper();
		String group_id = chatBody.getGroupId();
		//先将群消息持久化到存储Timeline;
		writeMessage(storeTable,GROUP+":"+group_id,chatBody);
		List<String> userIds = messageHelper.getGroupUsers(group_id);
		//通过写扩散模式将群消息同步到所有的群成员
		for(String userId : userIds){
			boolean isOnline = false;
			if(isStore && ImServerConfig.ON.equals(imServerConfig.getIsCluster())){
				isOnline = messageHelper.isOnline(userId);
			}else{
				isOnline = ChatKit.isOnline(userId, isStore);
			}
			if(!isOnline){
				writeMessage(pushTable, GROUP+":"+group_id+":"+userId, chatBody);
			}
		}
	}
	
	private void writeMessage(String timelineTable , String timelineId , ChatBody chatBody){
		MessageHelper messageHelper = imServerConfig.getMessageHelper();
		messageHelper.writeMessage(timelineTable, timelineId, chatBody);
	}
}
