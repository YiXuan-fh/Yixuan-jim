/**
 * 
 */
package org.jim.core.tcp;

import java.nio.ByteBuffer;

import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.ImSessionContext;
import org.jim.core.exception.ImException;
import org.jim.core.protocol.AbstractProtocol;
import org.jim.core.protocol.IProtocolConverter;
import org.jim.core.utils.ImKit;

/**
 * @desc Tcp协议校验器
 * @author WChao
 * @date 2018-05-01
 */
public class TcpProtocol extends AbstractProtocol {

	public TcpProtocol(IProtocolConverter converter){
		super(converter);
	}

	@Override
	public String name() {
		return Protocol.TCP;
	}

	@Override
	protected void init(ImChannelContext imChannelContext) {
		imChannelContext.setSessionContext(new TcpSessionContext(imChannelContext));
		ImKit.initImClientNode(imChannelContext);
	}

	@Override
	public boolean validateProtocol(ImSessionContext imSessionContext) throws ImException {
		if(imSessionContext instanceof TcpSessionContext){
			return true;
		}
		return false;
	}

	@Override
	public boolean validateProtocol(ByteBuffer buffer, ImChannelContext imChannelContext) throws ImException {
		//获取第一个字节协议版本号,TCP协议;
		if(buffer.get() == Protocol.VERSION){
			return true;
		}
		return false;
	}

	@Override
	public boolean validateProtocol(ImPacket imPacket) throws ImException {
		if(imPacket instanceof TcpPacket){
			return true;
		}
		return false;
	}

}
