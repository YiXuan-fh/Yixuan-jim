/**
 * 
 */
package org.jim.server.protocol.http;

import org.jim.core.ImChannelContext;
import org.jim.core.ImConst;
import org.jim.core.ImPacket;
import org.jim.core.config.ImConfig;
import org.jim.core.exception.ImDecodeException;
import org.jim.core.exception.ImException;
import org.jim.core.http.*;
import org.jim.core.http.handler.IHttpRequestHandler;
import org.jim.core.protocol.AbstractProtocol;
import org.jim.core.session.id.impl.UUIDSessionIdGenerator;
import org.jim.server.JimServer;
import org.jim.server.JimServerAPI;
import org.jim.server.config.ImServerConfig;
import org.jim.server.protocol.AbstractProtocolHandler;
import org.jim.server.protocol.http.mvc.Routes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.utils.cache.guava.GuavaCache;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * 版本: [1.0]
 * 功能说明: 
 * @author : WChao 创建时间: 2017年8月3日 下午3:07:54
 */
public class HttpProtocolHandler extends AbstractProtocolHandler {
	
	private Logger log = LoggerFactory.getLogger(HttpProtocolHandler.class);

	private HttpConfig httpConfig;
	
	private IHttpRequestHandler httpRequestHandler;

	public HttpProtocolHandler(){
		this(null, new HttpProtocol(new HttpConvertPacket()));
	};

	public HttpProtocolHandler(HttpConfig httpConfig, AbstractProtocol protocol){
		super(protocol);
		this.httpConfig = httpConfig;
	}

	@Override
	public void init(ImServerConfig imServerConfig)throws ImException {
		this.httpConfig = imServerConfig.getHttpConfig();
		if (Objects.isNull(httpConfig.getSessionStore())) {
			GuavaCache guavaCache = GuavaCache.register(httpConfig.getSessionCacheName(), null, httpConfig.getSessionTimeout());
			httpConfig.setSessionStore(guavaCache);
		}
		if (Objects.isNull(httpConfig.getSessionIdGenerator())) {
			httpConfig.setSessionIdGenerator(UUIDSessionIdGenerator.instance);
		}
		if(Objects.isNull(httpConfig.getScanPackages())){
			//J-IM MVC需要扫描的根目录包
			String[] scanPackages = new String[] { JimServer.class.getPackage().getName() };
			httpConfig.setScanPackages(scanPackages);
		}else{
			String[] scanPackages = new String[httpConfig.getScanPackages().length+1];
			scanPackages[0] = JimServer.class.getPackage().getName();
			System.arraycopy(httpConfig.getScanPackages(), 0, scanPackages, 1, httpConfig.getScanPackages().length);
			httpConfig.setScanPackages(scanPackages);
		}
		Routes routes = new Routes(httpConfig.getScanPackages());
		httpRequestHandler = new DefaultHttpRequestHandler(httpConfig, routes);
		httpConfig.setHttpRequestHandler(httpRequestHandler);
		log.info("Http Protocol initialized");
	}
	
	@Override
	public ByteBuffer encode(ImPacket imPacket, ImConfig imConfig, ImChannelContext imChannelContext) {
		HttpResponse httpResponsePacket = (HttpResponse) imPacket;
		ByteBuffer byteBuffer = HttpResponseEncoder.encode(httpResponsePacket, imChannelContext,false);
		return byteBuffer;
	}

	@Override
	public void handler(ImPacket imPacket, ImChannelContext imChannelContext)throws ImException {
		HttpRequest httpRequestPacket = (HttpRequest) imPacket;
		HttpResponse httpResponsePacket = httpRequestHandler.handler(httpRequestPacket, httpRequestPacket.getRequestLine());
		JimServerAPI.send(imChannelContext, httpResponsePacket);
	}

	@Override
	public ImPacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ImChannelContext imChannelContext)throws ImDecodeException {
		HttpRequest request = HttpRequestDecoder.decode(buffer, imChannelContext,true);
		imChannelContext.setAttribute(ImConst.HTTP_REQUEST,request);
		return request;
	}
	
	public IHttpRequestHandler getHttpRequestHandler() {
		return httpRequestHandler;
	}

	public void setHttpRequestHandler(IHttpRequestHandler httpRequestHandler) {
		this.httpRequestHandler = httpRequestHandler;
	}
	
	public HttpConfig getHttpConfig() {
		return httpConfig;
	}

	public void setHttpConfig(HttpConfig httpConfig) {
		this.httpConfig = httpConfig;
	}

}
