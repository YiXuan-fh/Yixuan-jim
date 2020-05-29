/**
 * 
 */
package org.jim.core.protocol;

import java.nio.ByteBuffer;
import java.util.Objects;

import org.jim.core.ImChannelContext;
import org.jim.core.ImConst;
import org.jim.core.ImPacket;
import org.jim.core.ImSessionContext;
import org.jim.core.exception.ImException;
/**
 * 协议校验接口抽象类
 * @author WChao
 * @date 2018-09-05 23:52:00
 */
public abstract class AbstractProtocol implements IProtocol,ImConst {
	/**
	 * 协议包转化器;
	 */
	protected IProtocolConverter converter;

	public AbstractProtocol(IProtocolConverter converter){
		this.converter = converter;
	}
	/**
	 * 协议初始化
	 * @param imChannelContext
	 */
	protected abstract void init(ImChannelContext imChannelContext);
	/**
	 * 根据buffer判断是否属于指定协议
	 * @param buffer
	 * @param imChannelContext
	 * @return
	 * @throws ImException
	 */
	protected abstract boolean validateProtocol(ByteBuffer buffer, ImChannelContext imChannelContext) throws ImException;

	/**
	 * 根据SessionContext判断协议
	 * @param imSessionContext
	 * @return
	 * @throws ImException
	 */
	protected abstract boolean validateProtocol(ImSessionContext imSessionContext) throws ImException;

	/**
	 * 根据imPacket判断是否属于指定协议
	 * @param imPacket
	 * @return
	 * @throws ImException
	 */
	protected abstract boolean validateProtocol(ImPacket imPacket) throws ImException;

	@Override
	public boolean isProtocol(ByteBuffer buffer, ImChannelContext imChannelContext) throws ImException {
		ImSessionContext imSessionContext = imChannelContext.getSessionContext();
		if(Objects.isNull(imSessionContext) && Objects.isNull(buffer)){
			return false;
		}else if(Objects.isNull(imSessionContext) && Objects.nonNull(buffer)){
			boolean isProtocol = validateProtocol(ByteBuffer.wrap(buffer.array()), imChannelContext);
			if(isProtocol){
				init(imChannelContext);
			}
			return isProtocol;
		}else{
			return validateProtocol(imSessionContext);
		}
	}

	@Override
	public boolean isProtocol(ImPacket imPacket, ImChannelContext imChannelContext) throws ImException {
		ImSessionContext sessionContext = imChannelContext.getSessionContext();
		if(Objects.isNull(imPacket)){
			return false;
		}
		boolean isProtocol = validateProtocol(imPacket);
		if(isProtocol && Objects.isNull(sessionContext)) {
			init(imChannelContext);
		}
		return isProtocol;
	}

	public IProtocolConverter getConverter() {
		return converter;
	}

}
