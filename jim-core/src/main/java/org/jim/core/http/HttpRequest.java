package org.jim.core.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.jim.core.http.session.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Node;
import org.tio.utils.hutool.ArrayUtil;

/**
 *
 * @author wchao
 *
 */
public class HttpRequest extends HttpPacket {

	private static Logger log = LoggerFactory.getLogger(HttpRequest.class);

	private static final long serialVersionUID = -3849253977016967211L;

	/**
	 * @param args
	 *
	 * @author wchao
	 * 2017年2月22日 下午4:14:40
	 *
	 */
	public static void main(String[] args) {
	}

	private RequestLine requestLine = null;
	/**
	 * 请求参数
	 */
	private Map<String, Object[]> params = new HashMap<>();;
	private List<Cookie> cookies = null;
	private Map<String, Cookie> cookieMap = null;
	private int contentLength;
	private String bodyString;
	private Http.RequestBodyFormat bodyFormat;
	private String charset = Http.CHARSET_NAME;
	private Boolean isAjax = null;
	private Boolean isSupportGzip = null;
	private HttpSession httpSession;
	private Node remote;
	private HttpConfig httpConfig;

	/**
	 *
	 *
	 * @author wchao
	 * 2017年2月22日 下午4:14:40
	 *
	 */
	public HttpRequest(Node remote) {
		this.remote = remote;
	}

	public void addParam(String key, Object value) {
		if (params == null) {
			params = new HashMap<>();
		}

		Object[] existValue = params.get(key);
		if (existValue != null) {
			Object[] newExistValue = new Object[existValue.length + 1];
			System.arraycopy(existValue, 0, newExistValue, 0, existValue.length);
			newExistValue[newExistValue.length - 1] = value;
			params.put(key, newExistValue);
		} else {
			Object[] newExistValue = new Object[] { value };
			params.put(key, newExistValue);
		}
	}

	/**
	 * @return the bodyFormat
	 */
	public Http.RequestBodyFormat getBodyFormat() {
		return bodyFormat;
	}

	/**
	 * @return the bodyString
	 */
	public String getBodyString() {
		return bodyString;
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @return the bodyLength
	 */
	public int getContentLength() {
		return contentLength;
	}

	public Cookie getCookie(String cooieName) {
		if (cookieMap == null) {
			return null;
		}
		return cookieMap.get(cooieName);
	}

	/**
	 * @return the cookieMap
	 */
	public Map<String, Cookie> getCookieMap() {
		return cookieMap;
	}

	/**
	 * @return the cookies
	 */
	public List<Cookie> getCookies() {
		return cookies;
	}

	/**
	 * @return the httpConfig
	 */
	public HttpConfig getHttpConfig() {
		return httpConfig;
	}

	/**
	 * @return the httpSession
	 */
	public HttpSession getHttpSession() {
		return httpSession;
	}

	/**
	 * @return the isAjax
	 */
	public Boolean getIsAjax() {
		if (isAjax == null) {
			String X_Requested_With = this.getHeader(Http.RequestHeaderKey.X_Requested_With);
			if (X_Requested_With != null && "XMLHttpRequest".equalsIgnoreCase(X_Requested_With)) {
				isAjax = true;
			} else {
				isAjax = false;
			}
		}

		return isAjax;
	}

	/**
	 * @return the isSupportGzip
	 */
	public Boolean getIsSupportGzip() {
		if (isSupportGzip == null) {
			String Accept_Encoding = getHeader(Http.RequestHeaderKey.Accept_Encoding);
			if (StringUtils.isNoneBlank(Accept_Encoding)) {
				String[] ss = StringUtils.split(Accept_Encoding, ",");
				if (ArrayUtil.contains(ss, "gzip")) {
					isSupportGzip = true;
				} else {
					isSupportGzip = false;
				}
			} else {
				isSupportGzip = true;
			}
		}
		return isSupportGzip;
	}

	/**
	 * @return the params
	 */
	public Map<String, Object[]> getParams() {
		return params;
	}

	public Node getRemote() {
		return remote;
	}

	/**
	 * @return the firstLine
	 */
	public RequestLine getRequestLine() {
		return requestLine;
	}

	/**
	 * @return
	 * @author wchao
	 */
	@Override
	public String logstr() {
		String str = "\r\n请求ID_" + getId() + "\r\n" + getHeaderString();
		if (null != getBodyString()) {
			str += getBodyString();
		}
		return str;
	}

	public void parseCookie() {
		String cookieLine = headers.get(Http.RequestHeaderKey.Cookie);
		if (StringUtils.isNotBlank(cookieLine)) {
			cookies = new ArrayList<>();
			cookieMap = new HashMap<>();
			Map<String, String> _cookieMap = Cookie.getEqualMap(cookieLine);
			List<Map<String, String>> cookieListMap = new ArrayList<>();
			for (Entry<String, String> cookieMapEntry : _cookieMap.entrySet()) {
				HashMap<String, String> cookieOneMap = new HashMap<>();
				cookieOneMap.put(cookieMapEntry.getKey(), cookieMapEntry.getValue());
				cookieListMap.add(cookieOneMap);

				Cookie cookie = Cookie.buildCookie(cookieOneMap);
				cookies.add(cookie);
				cookieMap.put(cookie.getName(), cookie);
				log.info("{}, 收到cookie:{}", imChannelContext, cookie.toString());
			}
		}
	}

	/**
	 * @param bodyFormat the bodyFormat to set
	 */
	public void setBodyFormat(Http.RequestBodyFormat bodyFormat) {
		this.bodyFormat = bodyFormat;
	}

	/**
	 * @param bodyString the bodyString to set
	 */
	public void setBodyString(String bodyString) {
		this.bodyString = bodyString;
	}

	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @param contentLength the bodyLength to set
	 */
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	/**
	 * @param cookieMap the cookieMap to set
	 */
	public void setCookieMap(Map<String, Cookie> cookieMap) {
		this.cookieMap = cookieMap;
	}

	/**
	 * @param cookies the cookies to set
	 */
	public void setCookies(List<Cookie> cookies) {
		this.cookies = cookies;
	}

	/**
	 * 设置好header后，会把cookie等头部信息也设置好
	 * @param headers the headers to set
	 */
	@Override
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
		if (headers != null) {
			parseCookie();
		}
	}

	/**
	 * @param httpConfig the httpConfig to set
	 */
	public void setHttpConfig(HttpConfig httpConfig) {
		this.httpConfig = httpConfig;
	}

	/**
	 * @param httpSession the httpSession to set
	 */
	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	/**
	 * @param isAjax the isAjax to set
	 */
	public void setIsAjax(Boolean isAjax) {
		this.isAjax = isAjax;
	}

	/**
	 * @param isSupportGzip the isSupportGzip to set
	 */
	public void setIsSupportGzip(Boolean isSupportGzip) {
		this.isSupportGzip = isSupportGzip;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Map<String, Object[]> params) {
		this.params = params;
	}

	public void setRemote(Node remote) {
		this.remote = remote;
	}

	/**
	 * @param requestLine the requestLine to set
	 */
	public void setRequestLine(RequestLine requestLine) {
		this.requestLine = requestLine;
	}

}
