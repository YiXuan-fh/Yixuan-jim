package org.jim.client;

import java.nio.ByteBuffer;
import org.jim.client.handler.ImClientHandler;
import org.jim.core.ImChannelContext;
import org.jim.core.ImConst;
import org.jim.core.ImPacket;
import org.jim.core.config.ImConfig;
import org.jim.core.exception.ImDecodeException;
import org.jim.core.packets.Command;
import org.jim.core.tcp.TcpPacket;
import org.jim.core.tcp.TcpServerDecoder;
import org.jim.core.tcp.TcpServerEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年8月30日 下午1:10:28
 */
public class HelloImClientHandler implements ImClientHandler, ImConst
{
	private static Logger logger = LoggerFactory.getLogger(HelloImClientHandler.class);

	/** 
	 * 处理消息
	 */
	@Override
	public void handler(ImPacket imPacket, ImChannelContext channelContext){
		TcpPacket helloPacket = (TcpPacket)imPacket;
		byte[] body = helloPacket.getBody();
		if (body != null)
		{
			try {
				String str = new String(body, ImConst.CHARSET);
				logger.info("demo客户端收到消息:{}", str);
			}catch (Exception e){
				logger.error(e.getMessage(), e);
			}
		}

		return;
	}

	/**
	 * 编码：把业务消息包编码为可以发送的ByteBuffer
	 * 总的消息结构：消息头 + 消息体
	 * 消息头结构：    4个字节，存储消息体的长度
	 * 消息体结构：   对象的json串的byte[]
	 */
	@Override
	public ByteBuffer encode(ImPacket imPacket, ImConfig imConfig, ImChannelContext imChannelContext)
	{
		TcpPacket tcpPacket = (TcpPacket)imPacket;
		return TcpServerEncoder.encode(tcpPacket, imConfig, imChannelContext);
	}
	
	@Override
	public TcpPacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ImChannelContext imChannelContext) throws ImDecodeException {
		TcpPacket tcpPacket = TcpServerDecoder.decode(buffer, imChannelContext);
		return tcpPacket;
	}
	
	private static TcpPacket heartbeatPacket = new TcpPacket(Command.COMMAND_HEARTBEAT_REQ,new byte[]{Protocol.HEARTBEAT_BYTE});

	/** 
	 * 此方法如果返回null，框架层面则不会发心跳；如果返回非null，框架层面会定时发本方法返回的消息包
	 */
	@Override
	public TcpPacket heartbeatPacket(ImChannelContext imChannelContext)
	{
		return heartbeatPacket;
	}

}
