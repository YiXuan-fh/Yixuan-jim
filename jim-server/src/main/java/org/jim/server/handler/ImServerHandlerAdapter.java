package org.jim.server.handler;

import org.jim.core.ImChannelContext;
import org.jim.core.ImConst;
import org.jim.core.ImPacket;
import org.jim.core.config.ImConfig;
import org.jim.core.exception.ImDecodeException;
import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.nio.ByteBuffer;

/**
 * @ClassName ImServerHandlerAdapter
 * @Description TODO
 * @Author WChao
 * @Date 2020/1/6 2:30
 * @Version 1.0
 **/
public class ImServerHandlerAdapter implements ServerAioHandler, ImConst{

    private ImServerHandler imServerHandler;

    public ImServerHandlerAdapter(ImServerHandler imServerHandler){
        this.imServerHandler = imServerHandler;
    }

    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        ImPacket imPacket;
        try {
            imPacket = this.imServerHandler.decode(buffer, limit, position, readableLength, (ImChannelContext)channelContext.get(Key.IM_CHANNEL_CONTEXT_KEY));
        }catch (ImDecodeException e) {
            throw new AioDecodeException(e);
        }
        return imPacket;
    }

    @Override
    public ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext) {
        return this.imServerHandler.encode((ImPacket)packet, ImConfig.Global.get(), (ImChannelContext)channelContext.get(Key.IM_CHANNEL_CONTEXT_KEY));
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        this.imServerHandler.handler((ImPacket)packet, (ImChannelContext)channelContext.get(Key.IM_CHANNEL_CONTEXT_KEY));
    }

}
