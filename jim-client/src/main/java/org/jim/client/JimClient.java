/**
 * 
 */
package org.jim.client;

import org.jim.client.config.ImClientConfig;
import org.jim.core.ImConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientTioConfig;
import org.tio.client.TioClient;
import org.tio.core.Node;
import java.util.Objects;

/**
 * J-IM客户端连接类
 * @author WChao
 *
 */
public class JimClient {

	private static Logger log = LoggerFactory.getLogger(JimClient.class);

	private TioClient tioClient = null;
	private ImClientConfig imClientConfig;

	public JimClient(ImClientConfig imClientConfig){
		this.imClientConfig = imClientConfig;
	}

	public ImClientChannelContext connect(Node serverNode) throws Exception {
		return connect(serverNode, null);
	}

	public ImClientChannelContext connect(Node serverNode, Integer timeout) throws Exception {
		log.warn("J-IM client connect");
		tioClient = new TioClient((ClientTioConfig) imClientConfig.getTioConfig());
		ClientChannelContext clientChannelContext = tioClient.connect(serverNode, imClientConfig.getBindIp(), imClientConfig.getBindPort(), timeout);
		if(Objects.nonNull(clientChannelContext)){
			log.warn("J-IM client connected success at serverAddress:[{}], bind localAddress:[{}]", serverNode.toString(), imClientConfig.toBindAddressString());
			return (ImClientChannelContext)clientChannelContext.get(ImConst.Key.IM_CHANNEL_CONTEXT_KEY);
		}
		return null;
	}

	public void stop(){
		tioClient.stop();
	}

}
