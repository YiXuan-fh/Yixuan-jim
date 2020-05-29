package org.jim.server.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jim.core.ImConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jim.core.http.HttpConfig;
import org.jim.core.http.HttpConst;
import org.jim.core.http.HttpRequest;
import org.jim.core.http.HttpResponse;
import org.jim.core.http.HttpResponseStatus;
import org.jim.core.http.MimeType;
import org.tio.utils.json.Json;

import cn.hutool.core.io.FileUtil;

/**
 * @author WChao
 * 2017年6月29日 下午4:17:24
 */
public class HttpResps implements ImConst.Http {
	private static Logger log = LoggerFactory.getLogger(HttpResps.class);

	/**
	 * Content-Type: text/css; charset=utf-8
	 * @param request
	 * @param bodyString
	 * @return
	 * @author WChao
	 */
	public static HttpResponse css(HttpRequest request, String bodyString) {
		return css(request, bodyString, HttpServerUtils.getHttpConfig(request).getCharset());
	}

	/**
	 * Content-Type: text/css; charset=utf-8
	 * @param request
	 * @param bodyString
	 * @param charset
	 * @return
	 * @author WChao
	 */
	public static HttpResponse css(HttpRequest request, String bodyString, String charset) {
		HttpResponse ret = string(request, bodyString, charset, MimeType.TEXT_CSS_CSS.getType() + "; charset=" + charset);
		return ret;
	}

	/**
	 * 根据文件内容创建响应
	 * @param request
	 * @param bodyBytes
	 * @param extension
	 * @return
	 * @author WChao
	 */
	public static HttpResponse file(HttpRequest request, byte[] bodyBytes, String extension) {
		String contentType = null;
//		String extension = FilenameUtils.getExtension(filename);
		if (StringUtils.isNoneBlank(extension)) {
			MimeType mimeType = MimeType.fromExtension(extension);
			if (mimeType != null) {
				contentType = mimeType.getType();
			} else {
				contentType = "application/octet-stream";
			}
		}
		return fileWithContentType(request, bodyBytes, contentType);
	}

	/**
	 * 根据文件创建响应
	 * @param request
	 * @param fileOnServer
	 * @return
	 * @throws IOException
	 * @author WChao
	 */
	public static HttpResponse file(HttpRequest request, File fileOnServer) throws IOException {
		Date lastModified = FileUtil.lastModifiedTime(fileOnServer);
		HttpResponse ret = try304(request, lastModified.getTime());
		if (ret != null) {
			return ret;
		}

		byte[] bodyBytes = FileUtil.readBytes(fileOnServer);
		String filename = fileOnServer.getName();
		String extension = FilenameUtils.getExtension(filename);
		ret = file(request, bodyBytes, extension);
		ret.addHeader(ResponseHeaderKey.Last_Modified, lastModified.getTime() + "");
		return ret;
	}

	/**
	 *
	 * @param request
	 * @param bodyBytes
	 * @param contentType 形如:application/octet-stream等
	 * @return
	 * @author WChao
	 */
	public static HttpResponse fileWithContentType(HttpRequest request, byte[] bodyBytes, String contentType) {
		HttpResponse ret = new HttpResponse(request, HttpServerUtils.getHttpConfig(request));
		ret.setBodyAndGzip(bodyBytes, request);
		ret.addHeader(ResponseHeaderKey.Content_Type, contentType);
		return ret;
	}

	/**
	 *
	 * @param request
	 * @param bodyBytes
	 * @param headers
	 * @return
	 * @author WChao
	 */
	public static HttpResponse fileWithHeaders(HttpRequest request, byte[] bodyBytes, Map<String, String> headers, HttpConfig httpConfig) {
		HttpResponse ret = new HttpResponse(request, httpConfig);
		ret.setBodyAndGzip(bodyBytes, request);
		ret.addHeaders(headers);
		return ret;
	}

	/**
	 *
	 * @param request
	 * @param bodyString
	 * @return
	 * @author WChao
	 */
	public static HttpResponse html(HttpRequest request, String bodyString) {
		HttpConfig httpConfig = HttpServerUtils.getHttpConfig(request);
		return html(request, bodyString, httpConfig.getCharset());
	}

	/**
	 * Content-Type: text/html; charset=utf-8
	 * @param request
	 * @param bodyString
	 * @param charset
	 * @return
	 * @author WChao
	 */
	public static HttpResponse html(HttpRequest request, String bodyString, String charset) {
		HttpResponse ret = string(request, bodyString, charset, MimeType.TEXT_HTML_HTML.getType() + "; charset=" + charset);
		return ret;
	}

	/**
	 * Content-Type: application/javascript; charset=utf-8
	 * @param request
	 * @param bodyString
	 * @return
	 * @author WChao
	 */
	public static HttpResponse js(HttpRequest request, String bodyString) {
		return js(request, bodyString, HttpServerUtils.getHttpConfig(request).getCharset());
	}

	/**
	 * Content-Type: application/javascript; charset=utf-8
	 * @param request
	 * @param bodyString
	 * @param charset
	 * @return
	 * @author WChao
	 */
	public static HttpResponse js(HttpRequest request, String bodyString, String charset) {
		HttpResponse ret = string(request, bodyString, charset, MimeType.APPLICATION_JAVASCRIPT_JS.getType() + "; charset=" + charset);
		return ret;
	}

	/**
	 * Content-Type: application/json; charset=utf-8
	 * @param request
	 * @param body
	 * @return
	 * @author WChao
	 */
	public static HttpResponse json(HttpRequest request, Object body) {
		return json(request, body, HttpServerUtils.getHttpConfig(request).getCharset());
	}

	/**
	 * Content-Type: application/json; charset=utf-8
	 * @param request
	 * @param body
	 * @param charset
	 * @return
	 * @author WChao
	 */
	public static HttpResponse json(HttpRequest request, Object body, String charset) {
		HttpResponse ret = null;
		if (body == null) {
			ret = string(request, "", charset, MimeType.TEXT_PLAIN_JSON.getType() + "; charset=" + charset);
		} else {
			if (body.getClass() == String.class) {
				ret = string(request, (String) body, charset, MimeType.TEXT_PLAIN_JSON.getType() + "; charset=" + charset);
			} else {
				ret = string(request, Json.toJson(body), charset, MimeType.TEXT_PLAIN_JSON.getType() + "; charset=" + charset);
			}
		}

		return ret;
	}

	/**
	 * @param args
	 * @author WChao
	 */
	public static void main(String[] args) {

	}

	/**
	 * 重定向
	 * @param request
	 * @param path
	 * @return
	 * @author WChao
	 */
	public static HttpResponse redirect(HttpRequest request, String path) {
		HttpResponse ret = new HttpResponse(request, HttpServerUtils.getHttpConfig(request));
		ret.setStatus(HttpResponseStatus.C302);
		ret.addHeader(ResponseHeaderKey.Location, path);
		return ret;
	}

	/**
	 * 创建字符串输出
	 * @param request
	 * @param bodyString
	 * @param Content_Type
	 * @return
	 * @author WChao
	 */
	public static HttpResponse string(HttpRequest request, String bodyString, String Content_Type) {
		return string(request, bodyString, HttpServerUtils.getHttpConfig(request).getCharset(), Content_Type);
	}

	/**
	 * 创建字符串输出
	 * @param request
	 * @param bodyString
	 * @param charset
	 * @param Content_Type
	 * @return
	 * @author WChao
	 */
	public static HttpResponse string(HttpRequest request, String bodyString, String charset, String Content_Type) {
		HttpResponse ret = new HttpResponse(request, HttpServerUtils.getHttpConfig(request));
		if (bodyString != null) {
			try {
				ret.setBodyAndGzip(bodyString.getBytes(charset), request);
			} catch (UnsupportedEncodingException e) {
				log.error(e.toString(), e);
			}
		}
		ret.addHeader(ResponseHeaderKey.Content_Type, Content_Type);
		return ret;
	}

	/**
	 * 尝试返回304
	 * @param request
	 * @param lastModifiedOnServer 服务器中资源的lastModified
	 * @return
	 * @author WChao
	 */
	public static HttpResponse try304(HttpRequest request, long lastModifiedOnServer) {
		//If-Modified-Since
		String If_Modified_Since = request.getHeader(RequestHeaderKey.If_Modified_Since);
		if (StringUtils.isNoneBlank(If_Modified_Since)) {
			Long If_Modified_Since_Date = null;
			try {
				If_Modified_Since_Date = Long.parseLong(If_Modified_Since);

				if (lastModifiedOnServer <= If_Modified_Since_Date) {
					HttpResponse ret = new HttpResponse(request, HttpServerUtils.getHttpConfig(request));
					ret.setStatus(HttpResponseStatus.C304);
					return ret;
				}
			} catch (NumberFormatException e) {
				log.warn("{}, {}不是整数，浏览器信息:{}", request.getRemote(), If_Modified_Since, request.getHeader(RequestHeaderKey.User_Agent));
				return null;
			}
		}

		return null;
	}

	/**
	 * Content-Type: text/plain; charset=utf-8
	 * @param request
	 * @param bodyString
	 * @return
	 * @author WChao
	 */
	public static HttpResponse txt(HttpRequest request, String bodyString) {
		return txt(request, bodyString, HttpServerUtils.getHttpConfig(request).getCharset());
	}

	/**
	 * Content-Type: text/plain; charset=utf-8
	 * @param request
	 * @param bodyString
	 * @param charset
	 * @return
	 * @author WChao
	 */
	public static HttpResponse txt(HttpRequest request, String bodyString, String charset) {
		HttpResponse ret = string(request, bodyString, charset, MimeType.TEXT_PLAIN_TXT.getType() + "; charset=" + charset);
		return ret;
	}
	/**
	 *
	 * @author WChao
	 */
	private HttpResps() {
	}
}
