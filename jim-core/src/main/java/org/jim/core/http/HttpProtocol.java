/**
 * 
 */
package org.jim.core.http;

import java.nio.ByteBuffer;
import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.ImSessionContext;
import org.jim.core.exception.ImException;
import org.jim.core.http.session.HttpSession;
import org.jim.core.protocol.AbstractProtocol;
import org.jim.core.protocol.IProtocolConverter;
import org.jim.core.utils.ImKit;

/**
 *
 * @desc Http协议校验器
 * @author WChao
 * @date 2018-05-01
 */
public class HttpProtocol extends AbstractProtocol {

	@Override
	public String name() {
		return Protocol.HTTP;
	}

	public HttpProtocol(IProtocolConverter protocolConverter){
		super(protocolConverter);
	}

	@Override
	protected void init(ImChannelContext imChannelContext) {
		imChannelContext.setSessionContext(new HttpSession(imChannelContext));
		ImKit.initImClientNode(imChannelContext);
	}

	@Override
	public boolean validateProtocol(ImSessionContext imSessionContext) throws ImException {
		if(imSessionContext instanceof HttpSession) {
			return true;
		}
		return false;
	}

	@Override
	public boolean validateProtocol(ByteBuffer buffer, ImChannelContext imChannelContext) throws ImException {
		HttpRequest request = HttpRequestDecoder.decode(buffer, imChannelContext,false);
		if(request.getHeaders().get(Http.RequestHeaderKey.Sec_WebSocket_Key) == null)
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean validateProtocol(ImPacket imPacket) throws ImException {
		if(imPacket instanceof HttpPacket){
			return true;
		}
		return false;
	}

}
