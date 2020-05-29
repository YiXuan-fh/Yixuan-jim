/**
 * 
 */
package org.jim.core.ws;

import java.nio.ByteBuffer;

import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.ImSessionContext;
import org.jim.core.exception.ImException;
import org.jim.core.http.HttpRequest;
import org.jim.core.http.HttpRequestDecoder;
import org.jim.core.protocol.AbstractProtocol;
import org.jim.core.protocol.IProtocolConverter;
import org.jim.core.utils.ImKit;

/**
 * WebSocket协议判断器
 * @author WChao
 *
 */
public class WsProtocol extends AbstractProtocol {

	@Override
	public String name() {
		return Protocol.WEB_SOCKET;
	}

	public WsProtocol(IProtocolConverter converter){
		super(converter);
	}
	
	@Override
	protected void init(ImChannelContext imChannelContext) {
		imChannelContext.setSessionContext(new WsSessionContext(imChannelContext));
		ImKit.initImClientNode(imChannelContext);
	}

	@Override
	protected boolean validateProtocol(ImSessionContext imSessionContext) throws ImException {
		if(imSessionContext instanceof WsSessionContext) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean validateProtocol(ByteBuffer buffer, ImChannelContext imChannelContext) throws ImException {
		//第一次连接;
		HttpRequest request = HttpRequestDecoder.decode(buffer, imChannelContext,false);
		if(request.getHeaders().get(Http.RequestHeaderKey.Sec_WebSocket_Key) != null)
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean validateProtocol(ImPacket imPacket) throws ImException {
		if(imPacket instanceof WsPacket){
			return true;
		}
		return false;
	}

}
