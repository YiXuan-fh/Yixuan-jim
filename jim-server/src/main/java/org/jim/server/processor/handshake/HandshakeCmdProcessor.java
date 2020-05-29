/**
 * 
 */
package org.jim.server.processor.handshake;

import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.exception.ImException;
import org.jim.server.processor.MultiProtocolCmdProcessor;

/**
 * @ClassName HandshakeCmdProcessor
 * @Description TODO
 * @Author WChao
 * @Date 2019/6/13 3:57
 * @Version 1.0
 **/
public interface HandshakeCmdProcessor extends MultiProtocolCmdProcessor {
	/**
	 * 对httpResponsePacket参数进行补充并返回，如果返回null表示不想和对方建立连接，框架会断开连接，如果返回非null，框架会把这个对象发送给对方
	 * @param packet
	 * @param imChannelContext
	 * @return
	 * @throws ImException
	 * @author: Wchao
	 */
	 ImPacket handshake(ImPacket packet, ImChannelContext imChannelContext)  throws ImException;
	/**
	 * 握手成功后
	 * @param packet
	 * @param imChannelContext
	 * @throws ImException
	 * @author Wchao
	 */
	 void onAfterHandshake(ImPacket packet, ImChannelContext imChannelContext) throws ImException;

}
