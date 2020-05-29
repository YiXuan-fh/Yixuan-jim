package org.jim.client;

import org.jim.client.config.ImClientConfig;
import org.jim.core.ImConst;
import org.jim.core.packets.ChatBody;
import org.jim.core.packets.ChatType;
import org.jim.core.packets.Command;
import org.jim.core.packets.LoginReqBody;
import org.jim.core.tcp.TcpPacket;
import org.tio.core.Node;
/**
 * 
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年8月30日 下午1:05:17
 */
public class HelloClientStarter{

	public static ImClientChannelContext imClientChannelContext = null;
	
	/**
	 * 启动程序入口
	 */
	public static void main(String[] args) throws Exception {
		//服务器节点
		Node serverNode = new Node("localhost", ImConst.SERVER_PORT);
		//构建客户端配置信息
		ImClientConfig imClientConfig = ImClientConfig.newBuilder()
				//客户端业务回调器,不可以为NULL
				.clientHandler(new HelloImClientHandler())
				//客户端事件监听器，可以为null，但建议自己实现该接口
				.clientListener(new HelloImClientListener())
				//心跳时长不设置，就不发送心跳包
				//.heartbeatTimeout(5000)
				//断链后自动连接的，不想自动连接请设为null
				//.reConnConf(new ReconnConf(5000L))
				.build();
		//生成客户端对象;
		JimClient jimClient = new JimClient(imClientConfig);
		//连接服务端
		imClientChannelContext = jimClient.connect(serverNode);
		//连上后，发条消息玩玩
		send();
	}

	private static void send() throws Exception {
		byte[] loginBody = new LoginReqBody("hello_client","123").toByte();
		TcpPacket loginPacket = new TcpPacket(Command.COMMAND_LOGIN_REQ,loginBody);
		//先登录;
		JimClientAPI.send(imClientChannelContext, loginPacket);
		ChatBody chatBody = ChatBody.newBuilder()
				.from("hello_client")
				.to("admin")
				.msgType(0)
				.chatType(ChatType.CHAT_TYPE_PUBLIC.getNumber())
				.groupId("100")
				.content("Socket普通客户端消息测试!").build();
		TcpPacket chatPacket = new TcpPacket(Command.COMMAND_CHAT_REQ,chatBody.toByte());
		JimClientAPI.send(imClientChannelContext, chatPacket);
	}
}
