package org.jim.core.http.handler;

import org.jim.core.exception.ImException;
import org.jim.core.http.HttpRequest;
import org.jim.core.http.HttpResponse;
import org.jim.core.http.RequestLine;

/**
 *
 * @author wchao
 *
 */
public interface IHttpRequestHandler {
	/**
	 *
	 * @param packet
	 * @param requestLine
	 * @return
	 * @throws ImException
	 * @author wchao
	 */
	public HttpResponse handler(HttpRequest packet, RequestLine requestLine) throws ImException;

	/**
	 *
	 * @param request
	 * @param requestLine
	 * @return
	 * @author wchao
	 */
	public HttpResponse resp404(HttpRequest request, RequestLine requestLine);

	/**
	 *
	 * @param request
	 * @param requestLine
	 * @param throwable
	 * @return
	 * @author wchao
	 */
	public HttpResponse resp500(HttpRequest request, RequestLine requestLine, java.lang.Throwable throwable);
	
	/**
	 * 清空静态资源缓存，如果没有缓存，可以不处理
	 * @param request
	 * @author: wchao
	 */
	public void clearStaticResCache(HttpRequest request);
}
