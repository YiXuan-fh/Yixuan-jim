/**
 * 
 */
package org.jim.server.protocol;

import org.apache.commons.lang3.StringUtils;
import org.jim.core.ImChannelContext;
import org.jim.core.ImConst;
import org.jim.core.ImPacket;
import org.jim.core.ImStatus;
import org.jim.core.exception.ImException;
import org.jim.core.packets.Command;
import org.jim.core.packets.RespBody;
import org.jim.core.protocol.IProtocolConverter;
import org.jim.server.ImServerChannelContext;
import org.jim.server.config.ImServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * 版本: [1.0]
 * 功能说明: 
 * @author : WChao 创建时间: 2017年8月3日 下午2:40:24
 */
public class ProtocolManager implements ImConst{
	
	private static Logger logger = LoggerFactory.getLogger(ProtocolManager.class);
	
	private static Map<String, AbstractProtocolHandler> serverHandlers = new HashMap<String,AbstractProtocolHandler>();
	
	static{
		try {
			List<ProtocolHandlerConfiguration> configurations = ProtocolHandlerConfigurationFactory.parseConfiguration();
			init(configurations);
		} catch (Exception e) {
			logger.error(e.toString(),e);
		}
	}
	
	private static void init(List<ProtocolHandlerConfiguration> configurations) throws Exception{
		for(ProtocolHandlerConfiguration configuration : configurations){
			Class<AbstractProtocolHandler> serverHandlerClazz = (Class<AbstractProtocolHandler>)Class.forName(configuration.getServerHandler());
			AbstractProtocolHandler serverHandler = serverHandlerClazz.newInstance();
			addServerHandler(serverHandler);
		}
	}
	
	public static AbstractProtocolHandler addServerHandler(AbstractProtocolHandler serverHandler)throws ImException{
		if(Objects.isNull(serverHandler)){
			throw new ImException("ProtocolHandler must not null ");
		}
		return serverHandlers.put(serverHandler.getProtocol().name(),serverHandler);
	}
	
	public static AbstractProtocolHandler removeServerHandler(String name)throws ImException{
		if(StringUtils.isEmpty(name)){
			throw new ImException("server name must not empty");
		}
		return serverHandlers.remove(name);
	}
	
	public static AbstractProtocolHandler initProtocolHandler(ByteBuffer buffer, ImChannelContext imChannelContext){
		ImServerChannelContext imServerChannelContext = (ImServerChannelContext)imChannelContext;
		for(Entry<String,AbstractProtocolHandler> entry : serverHandlers.entrySet()){
			AbstractProtocolHandler protocolHandler = entry.getValue();
			try {
				if(protocolHandler.getProtocol().isProtocol(buffer, imServerChannelContext)){
					imServerChannelContext.setProtocolHandler(protocolHandler);
					return protocolHandler;
				}
			} catch (Throwable e) {
				logger.error(e.getMessage());
			}
		}
		return null;
	}
	
	public static <T> T getServerHandler(String name,Class<T> clazz){
		AbstractProtocolHandler serverHandler = serverHandlers.get(name);
		if(Objects.isNull(serverHandler)) {
			return null;
		}
		return (T)serverHandler;
	}

	public static void init(){
		init((ImServerConfig)ImServerConfig.Global.get());
	}
	public static void init(ImServerConfig imServerConfig){
		logger.info("start init protocol [{}]", ImConst.JIM);
		for(Entry<String,AbstractProtocolHandler> entry : serverHandlers.entrySet()){
			try {
				entry.getValue().init(imServerConfig);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		logger.info("init protocol is completed [{}]", ImConst.JIM);
	}

	public static class Converter{

		/**
		 * 功能描述：[转换不同协议响应包]
		 * @author：WChao 创建时间: 2017年9月21日 下午3:21:54
		 * @param respBody 响应消息体
		 * @param imChannelContext IM通道上下文
		 * @return
		 *
		 */
		public static ImPacket respPacket(RespBody respBody, ImChannelContext imChannelContext)throws ImException {
			if(Objects.isNull(respBody)) {
				throw new ImException("响应包体不能为空!");
			}
			return respPacket(respBody.toByte(), respBody.getCommand(), imChannelContext);
		}

		/**
		 * 功能描述：[转换不同协议响应包]
		 * @param body 消息体字节
		 * @param command 命令码
		 * @param imChannelContext IM通道上下文
		 * @return
		 * @throws ImException
		 */
		public static ImPacket respPacket(byte[] body, Command command, ImChannelContext imChannelContext)throws ImException{
			return getProtocolConverter(imChannelContext).RespPacket(body, command, imChannelContext);
		}

		/**
		 * 功能描述：[转换不同协议响应包]
		 * @param imPacket 消息包
		 * @param imChannelContext IM通道上下文
		 * @return
		 * @throws ImException
		 */
		public static ImPacket respPacket(ImPacket imPacket, ImChannelContext imChannelContext)throws ImException{
			return respPacket(imPacket, imPacket.getCommand(), imChannelContext);
		}

		/**
		 * 功能描述：[转换不同协议响应包]
		 * @author：WChao 创建时间: 2017年9月21日 下午3:21:54
		 * @param imPacket 消息包
		 * @param command 命令码
		 * @param imChannelContext IM通道上下文
		 * @return
		 *
		 */
		public static ImPacket respPacket(ImPacket imPacket,Command command, ImChannelContext imChannelContext)throws ImException{
			return  getProtocolConverter(imChannelContext).RespPacket(imPacket, command, imChannelContext);
		}

		/**
		 * 通过通道获取当前通道协议
		 * @param imChannelContext IM通道上下文
		 * @return
		 * @throws ImException
		 */
		private static IProtocolConverter getProtocolConverter(ImChannelContext imChannelContext) throws ImException{
			ImServerChannelContext serverChannelContext = (ImServerChannelContext)imChannelContext;
			AbstractProtocolHandler protocolHandler = serverChannelContext.getProtocolHandler();
			if(Objects.isNull(protocolHandler)){
				throw new ImException("协议[ProtocolHandler]未初始化,协议包转化失败");
			}
			IProtocolConverter converter = protocolHandler.getProtocol().getConverter();
			if(converter != null){
				return converter;
			}else {
				throw new ImException("未获取到协议转化器[ProtocolConverter]");
			}
		}

	}

	public static class Packet{
		/**
		 * 数据格式不正确响应包
		 * @param imChannelContext
		 * @return imPacket
		 * @throws ImException
		 */
		public static ImPacket  dataInCorrect(ImChannelContext imChannelContext) throws ImException{
			RespBody chatDataInCorrectRespPacket = new RespBody(Command.COMMAND_CHAT_RESP,ImStatus.C10002);
			ImPacket respPacket = Converter.respPacket(chatDataInCorrectRespPacket, imChannelContext);
			respPacket.setStatus(ImStatus.C10002);
			return respPacket;
		}

		/**
		 * 发送成功响应包
		 * @param imChannelContext
		 * @return imPacket
		 * @throws ImException
		 */
		public static ImPacket  success(ImChannelContext imChannelContext) throws ImException{
			RespBody chatDataInCorrectRespPacket = new RespBody(Command.COMMAND_CHAT_RESP, ImStatus.C10000);
			ImPacket respPacket = Converter.respPacket(chatDataInCorrectRespPacket, imChannelContext);
			respPacket.setStatus(ImStatus.C10000);
			return respPacket;
		}

		/**
		 * 用户不在线响应包
		 * @param imChannelContext
		 * @return
		 * @throws ImException
		 */
		public static ImPacket  offline(ImChannelContext imChannelContext) throws ImException{
			RespBody chatDataInCorrectRespPacket = new RespBody(Command.COMMAND_CHAT_RESP,ImStatus.C10001);
			ImPacket respPacket = Converter.respPacket(chatDataInCorrectRespPacket, imChannelContext);
			respPacket.setStatus(ImStatus.C10001);
			return respPacket;
		}
	}
}
