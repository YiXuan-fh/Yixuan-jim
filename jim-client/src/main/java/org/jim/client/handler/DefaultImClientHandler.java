package org.jim.client.handler;

import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.config.ImConfig;
import org.jim.core.exception.ImDecodeException;
import org.jim.core.exception.ImException;
import java.nio.ByteBuffer;

/**
 * @ClassName DefaultImClientHandler
 * @Description 默认的IM客户端回调
 * @Author WChao
 * @Date 2020/1/6 2:25
 * @Version 1.0
 **/
public class DefaultImClientHandler implements ImClientHandler {

    @Override
    public void handler(ImPacket imPacket, ImChannelContext imChannelContext) throws ImException {

    }

    @Override
    public ByteBuffer encode(ImPacket imPacket, ImConfig imConfig, ImChannelContext imChannelContext) {
        return null;
    }

    @Override
    public ImPacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ImChannelContext imChannelContext) throws ImDecodeException {
       return null;
    }

    @Override
    public ImPacket heartbeatPacket(ImChannelContext imChannelContext) {
        return null;
    }

}
