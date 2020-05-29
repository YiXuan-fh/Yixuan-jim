package org.jim.client.handler;

import org.jim.client.ImClientChannelContext;
import org.jim.core.ImConst;
import org.jim.core.ImPacket;
import org.jim.core.config.ImConfig;
import org.jim.core.exception.ImDecodeException;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import java.nio.ByteBuffer;

/**
 * @ClassName ImClientHandlerAdapter
 * @Description IM客户端回调适配器
 * @Author WChao
 * @Date 2020/1/6 2:30
 * @Version 1.0
 **/
public class ImClientHandlerAdapter implements ClientAioHandler, ImConst{

    private ImClientHandler imClientHandler;

    public ImClientHandlerAdapter(ImClientHandler imClientHandler){
        this.imClientHandler = imClientHandler;
    }

    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        ImPacket imPacket;
        try {
            imPacket = this.imClientHandler.decode(buffer, limit, position, readableLength, (ImClientChannelContext)channelContext.get(Key.IM_CHANNEL_CONTEXT_KEY));
        }catch (ImDecodeException e) {
            throw new AioDecodeException(e);
        }
        return imPacket;
    }

    @Override
    public ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext) {
        return this.imClientHandler.encode((ImPacket)packet, ImConfig.Global.get(), (ImClientChannelContext)channelContext.get(Key.IM_CHANNEL_CONTEXT_KEY));
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        this.imClientHandler.handler((ImPacket)packet, (ImClientChannelContext)channelContext.get(Key.IM_CHANNEL_CONTEXT_KEY));
    }

    @Override
    public Packet heartbeatPacket(ChannelContext channelContext) {
        return this.imClientHandler.heartbeatPacket((ImClientChannelContext)channelContext.get(Key.IM_CHANNEL_CONTEXT_KEY));
    }

}
