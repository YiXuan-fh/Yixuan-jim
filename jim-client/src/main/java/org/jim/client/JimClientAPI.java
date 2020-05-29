package org.jim.client;
import org.jim.core.ImChannelContext;
import org.jim.core.ImConst;
import org.jim.core.ImPacket;
import org.jim.core.config.ImConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Tio;
/**
 * 版本: [1.0]
 * 功能说明: JIM
 * @author : WChao 创建时间: 2017年9月22日 上午9:07:18
 */
public class JimClientAPI implements ImConst{

	public static ImConfig imConfig = ImConfig.Global.get();

	private static Logger log = LoggerFactory.getLogger(JimClientAPI.class);

	/**
	 * 功能描述：发送到群组
	 * @param groupId 群组ID
	 * @param packet 消息包
	 */
	public static void sendToGroup(String groupId, ImPacket packet){
		Tio.sendToGroup(imConfig.getTioConfig(), groupId, packet);
	}

	/**
	 * 发送到指定通道;
	 * @param imChannelContext IM通道上下文
	 * @param imPacket 消息包
	 */
	public static boolean send(ImChannelContext imChannelContext, ImPacket imPacket){
		if(imChannelContext == null){
			return false;
		}
		return Tio.send(imChannelContext.getTioChannelContext(),imPacket);
	}

	/**
	 * 阻塞发送（确认把packet发送到对端后再返回）
	 * @param imChannelContext IM通道上下文
	 * @param packet 消息包
	 * @return
	 */
	public static boolean bSend(ImChannelContext imChannelContext , ImPacket packet){
		if(imChannelContext == null){
			return false;
		}
		return Tio.bSend(imChannelContext.getTioChannelContext(), packet);
	}

	/**
	 * 关闭连接
	 * @param imChannelContext
	 * @param remark
	 */
	public static void close(ImChannelContext imChannelContext, String remark){
		Tio.close(imChannelContext.getTioChannelContext(), remark);
	}

}
