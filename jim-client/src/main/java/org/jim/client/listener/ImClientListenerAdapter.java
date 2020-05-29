package org.jim.client.listener;

import org.jim.client.ImClientChannelContext;
import org.jim.client.config.ImClientConfig;
import org.jim.core.ImConst;
import org.jim.core.ImPacket;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
/**
 * @ClassName ImClientListenerAdapter
 * @Description IM客户端连接监听适配器
 * @Author WChao
 * @Date 2020/1/4 9:35
 * @Version 1.0
 **/
public class ImClientListenerAdapter implements ClientAioListener, ImConst{
	/**
     * 客户端端监听器
	 */
	private ImClientListener imClientListener;

	public ImClientListenerAdapter(ImClientListener imClientListener) {
		this.imClientListener = imClientListener == null ? new DefaultImClientListener(): imClientListener;
	}

	@Override
	public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect)throws Exception{
		ImClientChannelContext imClientChannelContext = new ImClientChannelContext(ImClientConfig.Global.get(), channelContext);
		channelContext.set(Key.IM_CHANNEL_CONTEXT_KEY, imClientChannelContext);
		imClientListener.onAfterConnected(imClientChannelContext, isConnected, isReconnect);
	}

	@Override
	public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess)throws Exception{
		imClientListener.onAfterSent((ImClientChannelContext)channelContext.get(Key.IM_CHANNEL_CONTEXT_KEY), (ImPacket)packet, isSentSuccess);
	}

	@Override
	public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove)throws Exception{
		imClientListener.onBeforeClose((ImClientChannelContext)channelContext.get(Key.IM_CHANNEL_CONTEXT_KEY), throwable, remark, isRemove);
	}

	@Override
	public void onAfterDecoded(ChannelContext channelContext, Packet packet,int packetSize) throws Exception {
		imClientListener.onAfterDecoded((ImClientChannelContext)channelContext.get(Key.IM_CHANNEL_CONTEXT_KEY), (ImPacket)packet, packetSize);
	}

	@Override
	public void onAfterReceivedBytes(ChannelContext channelContext,int receivedBytes) throws Exception {
		imClientListener.onAfterReceivedBytes((ImClientChannelContext)channelContext.get(Key.IM_CHANNEL_CONTEXT_KEY), receivedBytes);
	}

	@Override
	public void onAfterHandled(ChannelContext channelContext, Packet packet,long cost) throws Exception {
		imClientListener.onAfterHandled((ImClientChannelContext)channelContext.get(Key.IM_CHANNEL_CONTEXT_KEY), (ImPacket)packet, cost);
	}

}
