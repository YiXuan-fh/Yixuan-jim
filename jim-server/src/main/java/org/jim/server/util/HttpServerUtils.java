package org.jim.server.util;
import org.jim.core.ImChannelContext;
import org.jim.core.ImSessionContext;
import org.jim.server.config.ImServerConfig;
import org.tio.core.ChannelContext;
import org.jim.core.http.HttpConfig;
import org.jim.core.http.HttpRequest;
/**
 * @author WChao
 * 2017年8月18日 下午5:47:00
 */
public class HttpServerUtils {
	/**
	 *
	 * @param request
	 * @return
	 * @author WChao
	 */
	public static HttpConfig getHttpConfig(HttpRequest request) {
		ImServerConfig imServerConfig = (ImServerConfig)request.getImChannelContext().getImConfig();
		return imServerConfig.getHttpConfig();
	}

	/**
	 * @param args
	 * @author WChao
	 */
	public static void main(String[] args) {

	}

	/**
	 *
	 * @author WChao
	 */
	public HttpServerUtils() {
	}
}
