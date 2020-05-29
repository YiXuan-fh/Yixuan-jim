package org.jim.client.handler;

import org.jim.core.ImChannelContext;
import org.jim.core.ImHandler;
import org.jim.core.ImPacket;

/**
 *
 * 客户端回调
 * @author WChao 
 *
 */
public interface ImClientHandler extends ImHandler {
    /**
     * 心跳包接口
     * @param imChannelContext
     * @return
     */
    ImPacket heartbeatPacket(ImChannelContext imChannelContext);
}
