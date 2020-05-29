package org.jim.core.http.listener;

import org.jim.core.http.HttpRequest;
import org.jim.core.http.HttpResponse;
import org.jim.core.http.RequestLine;
/**
 * @author wchao
 * 2017年7月25日 下午2:16:06
 */
public interface IHttpServerListener {

	/**
	 * 在执行org.tio.http.server.handler.IHttpRequestHandler.handler(HttpRequestPacket, RequestLine, ChannelContext<HttpSessionContext, HttpPacket, Object>)后会调用此方法，业务层可以统一在这里给HttpResponsePacket作一些修饰
	 * @param packet
	 * @param requestLine
	 * @param channelContext
	 * @param httpResponse
	 * @return
	 * @throws Exception
	 * @author wchao
	 */
	public void doAfterHandler(HttpRequest packet, RequestLine requestLine, HttpResponse httpResponse) throws Exception;

	/**
	 * 在执行org.tio.http.server.handler.IHttpRequestHandler.handler(HttpRequestPacket, RequestLine, ChannelContext<HttpSessionContext, HttpPacket, Object>)前会先调用这个方法<br>
	 * 如果返回了HttpResponsePacket对象，则后续都不再执行，表示调用栈就此结束<br>
	 * @param packet
	 * @param requestLine
	 * @param channelContext
	 * @param httpResponseFromCache 从缓存中获取到的HttpResponse对象
	 * @return
	 * @throws Exception
	 * @author wchao
	 */
	public HttpResponse doBeforeHandler(HttpRequest packet, RequestLine requestLine, HttpResponse httpResponseFromCache) throws Exception;

}
