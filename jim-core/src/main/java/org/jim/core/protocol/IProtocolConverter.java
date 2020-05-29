/**
 * 
 */
package org.jim.core.protocol;

import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.packets.Command;

/**
 * 转换不同协议消息包;
 * @author WChao
 *
 */
public interface IProtocolConverter {
	/**
	 * 转化请求包
	 * @param body
	 * @param command
	 * @param imChannelContext
	 * @return
	 */
	ImPacket ReqPacket(byte[] body, Command command, ImChannelContext imChannelContext);
	/**
	 * 转化响应包
	 * @param body
	 * @param command
	 * @param imChannelContext
	 * @return
	 */
	ImPacket RespPacket(byte[] body,Command command, ImChannelContext imChannelContext);

	/**
	 * 转化响应包
	 * @param imPacket
	 * @param command
	 * @param imChannelContext
	 * @return
	 */
	ImPacket RespPacket(ImPacket imPacket, Command command, ImChannelContext imChannelContext);
}
