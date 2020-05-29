package org.jim.client;

import org.jim.core.ImChannelContext;
import org.jim.core.config.ImConfig;
import org.tio.core.ChannelContext;

/**
 * @ClassName ImClientChannelContext
 * @Description 客户端通道上下文
 * @Author WChao
 * @Date 2020/1/5 23:56
 * @Version 1.0
 **/
public class ImClientChannelContext extends ImChannelContext {

    public ImClientChannelContext(ImConfig imConfig, ChannelContext tioChannelContext) {
        super(imConfig, tioChannelContext);
    }

}
