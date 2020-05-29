package org.jim.core.http;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jim.core.ImChannelContext;
import org.jim.core.ImConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wchao
 * 2017年8月4日 上午9:41:12
 */
public class HttpResponseEncoder implements ImConst {
	public enum Step {
		firstLine, header, body
	}

	private static Logger log = LoggerFactory.getLogger(HttpResponseEncoder.class);

	public static final int MAX_HEADER_LENGTH = 20480;

	/**
	 *
	 * @param httpResponse
	 * @param channelContext
	 * @param skipCookie true: 忽略掉cookie部分的编码
	 * @return
	 * @author WChao
	 */
	public static ByteBuffer encode(HttpResponse httpResponse, ImChannelContext channelContext, boolean skipCookie) {
		byte[] encodedBytes = httpResponse.getEncodedBytes();
		if (encodedBytes != null) {
			ByteBuffer ret = ByteBuffer.wrap(encodedBytes);
			ret.position(ret.limit());
			return ret;
		}

		int bodyLength = 0;
		byte[] body = httpResponse.getBody();
		if (body != null) {
			bodyLength = body.length;
		}

		StringBuilder sb = new StringBuilder(256);

		HttpResponseStatus httpResponseStatus = httpResponse.getStatus();
		//		httpResponseStatus.get
		sb.append("HTTP/1.1 ").append(httpResponseStatus.getStatus()).append(" ").append(httpResponseStatus.getDescription()).append("\r\n");

		Map<String, String> headers = httpResponse.getHeaders();
		if (headers != null && headers.size() > 0) {
			headers.put(Http.ResponseHeaderKey.Content_Length, bodyLength + "");
			Set<Entry<String, String>> set = headers.entrySet();
			for (Entry<String, String> entry : set) {
				sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
			}
		}

		if (!skipCookie) {
			//处理cookie
			List<Cookie> cookies = httpResponse.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					sb.append(Http.ResponseHeaderKey.Set_Cookie).append(": ");
					sb.append(cookie.toString());
					sb.append("\r\n");
					if (log.isDebugEnabled()) {
						log.debug("{}, set-cookie:{}", channelContext, cookie.toString());
					}
				}
			}
		}

		sb.append("\r\n");

		byte[] headerBytes = null;
		try {
			String headerString = sb.toString();
			httpResponse.setHeaderString(headerString);
			headerBytes = headerString.getBytes(httpResponse.getCharset());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ByteBuffer buffer = ByteBuffer.allocate(headerBytes.length + bodyLength);
		buffer.put(headerBytes);

		if (bodyLength > 0) {
			buffer.put(body);
		}
		return buffer;
	}

	/**
	 * @param args
	 *
	 * @author wchao
	 * 2017年2月22日 下午4:06:42
	 *
	 */
	public static void main(String[] args) {

	}

	/**
	 * 解析请求头的每一行
	 * @param line
	 * @return
	 *
	 * @author wchao
	 * 2017年2月23日 下午1:37:58
	 *
	 */
	public static KeyValue parseHeaderLine(String line) {
		KeyValue keyValue = new KeyValue();
		int p = line.indexOf(":");
		if (p == -1) {
			keyValue.setKey(line);
			return keyValue;
		}

		String name = line.substring(0, p).trim();
		String value = line.substring(p + 1).trim();

		keyValue.setKey(name);
		keyValue.setValue(value);

		return keyValue;
	}

	/**
	 *
	 *
	 * @author wchao
	 */
	public HttpResponseEncoder() {

	}

}
