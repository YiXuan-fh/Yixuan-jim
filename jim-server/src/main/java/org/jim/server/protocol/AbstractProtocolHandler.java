/**
 * 
 */
package org.jim.server.protocol;

import org.jim.core.exception.ImException;
import org.jim.core.protocol.AbstractProtocol;
import org.jim.server.config.ImServerConfig;

/**
 * 版本: [1.0] 功能说明: 抽象协议处理器;
 * @author : WChao 创建时间: 2017年8月3日 上午9:47:44
 */
public abstract class AbstractProtocolHandler implements IProtocolHandler {

	protected AbstractProtocol protocol;

	public AbstractProtocolHandler(){};

	public AbstractProtocolHandler(AbstractProtocol protocol){
		this.protocol = protocol;
	}

	/**
	 * 初始化不同协议
	 * @param imServerConfig
	 * @throws ImException
	 */
	public abstract void init(ImServerConfig imServerConfig)throws ImException;

	public AbstractProtocol getProtocol() {
		return protocol;
	}
}
