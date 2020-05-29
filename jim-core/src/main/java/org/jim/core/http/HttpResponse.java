package org.jim.core.http;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.utils.hutool.ZipUtil;

/**
 *
 * @author WChao
 *
 */
public class HttpResponse extends HttpPacket {
	private static Logger log = LoggerFactory.getLogger(HttpResponse.class);

	private static final long serialVersionUID = -3512681144230291786L;

	/**
	 * @param args
	 *
	 * @author WChao
	 * 2017年2月22日 下午4:14:40
	 *
	 */
	public static void main(String[] args) {
	}

	private HttpResponseStatus status = HttpResponseStatus.C200;

	/**
	 * 是否是静态资源
	 * true: 静态资源
	 */
	private boolean isStaticRes = false;

	private HttpRequest request;

	private volatile List<Cookie> cookies = null;

	private String charset = Http.CHARSET_NAME;

	/**
	 * 已经编码好的byte[]
	 */
	private byte[] encodedBytes = null;

	/**
	 *
	 * @param request
	 * @param httpConfig 可以为null
	 * @author wchao
	 */
	public HttpResponse(HttpRequest request, HttpConfig httpConfig) {
		this.request = request;

		String Connection = StringUtils.lowerCase(request.getHeader(Http.RequestHeaderKey.Connection));
		RequestLine requestLine = request.getRequestLine();
		String version = requestLine.getVersion();
		if ("1.0".equals(version)) {
			if (StringUtils.equals(Connection, Http.RequestHeaderValue.Connection.keep_alive)) {
				addHeader(Http.ResponseHeaderKey.Connection, Http.ResponseHeaderValue.Connection.keep_alive);
				addHeader(Http.ResponseHeaderKey.Keep_Alive, "timeout=10, max=20");
			} else {
				addHeader(Http.ResponseHeaderKey.Connection, Http.ResponseHeaderValue.Connection.close);
			}
		} else {
			if (StringUtils.equals(Connection, Http.RequestHeaderValue.Connection.close)) {
				addHeader(Http.ResponseHeaderKey.Connection, Http.ResponseHeaderValue.Connection.close);
			} else {
				addHeader(Http.ResponseHeaderKey.Connection, Http.ResponseHeaderValue.Connection.keep_alive);
				addHeader(Http.ResponseHeaderKey.Keep_Alive, "timeout=10, max=20");
			}
		}
		if (httpConfig != null) {
			addHeader(Http.ResponseHeaderKey.Server, httpConfig.getServerInfo());
		}
	}

	public boolean addCookie(Cookie cookie) {
		if (cookies == null) {
			synchronized (this) {
				if (cookies == null) {
					cookies = new ArrayList<>();
				}
			}
		}
		return cookies.add(cookie);
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @return the cookies
	 */
	public List<Cookie> getCookies() {
		return cookies;
	}

	/**
	 * @return the encodedBytes
	 */
	public byte[] getEncodedBytes() {
		return encodedBytes;
	}

	/**
	 * @return the request
	 */
	public HttpRequest getHttpRequestPacket() {
		return request;
	}

	/**
	 * @return the status
	 */
	@Override
	public HttpResponseStatus getStatus() {
		return status;
	}

	private void gzip(HttpRequest request) {
		if (request.getIsSupportGzip()) {
			byte[] bs = this.getBody();
			if (bs.length >= 600) {
				byte[] bs2 = ZipUtil.gzip(bs);
				if (bs2.length < bs.length) {
					this.body = bs2;
					this.addHeader(Http.ResponseHeaderKey.Content_Encoding, "gzip");
				}
			}
		} else {
			log.info("{} 竟然不支持gzip, {}", request.getImChannelContext(), request.getHeader(Http.RequestHeaderKey.User_Agent));
		}
	}

	/**
	 * @return the isStaticRes
	 */
	public boolean isStaticRes() {
		return isStaticRes;
	}

	@Override
	public String logstr() {
		String str = null;
		if (request != null) {
			str = "\r\n响应: 请求ID_" + request.getId() + "  " + request.getRequestLine().getPathAndQuery();
			str += "\r\n" + this.getHeaderString();
		} else {
			str = "\r\n响应\r\n" + status.getHeaderText();
		}
		return str;
	}

	public void setBody(byte[] body, HttpRequest request) {
		this.body = body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBodyAndGzip(byte[] body, HttpRequest request) {
		this.body = body;
		if (body != null) {
			gzip(request);
		}
	}

	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @param cookies the cookies to set
	 */
	public void setCookies(List<Cookie> cookies) {
		this.cookies = cookies;
	}

	/**
	 * @param encodedBytes the encodedBytes to set
	 */
	public void setEncodedBytes(byte[] encodedBytes) {
		this.encodedBytes = encodedBytes;
	}

	/**
	 * @param request the request to set
	 */
	public void setHttpRequestPacket(HttpRequest request) {
		this.request = request;
	}

	/**
	 * @param isStaticRes the isStaticRes to set
	 */
	public void setStaticRes(boolean isStaticRes) {
		this.isStaticRes = isStaticRes;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(HttpResponseStatus status) {
		this.status = status;
	}
}
