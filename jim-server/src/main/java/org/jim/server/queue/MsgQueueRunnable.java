package org.jim.server.queue;

import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.packets.Message;
import org.jim.server.processor.ProtocolCmdProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.utils.queue.FullWaitQueue;
import org.tio.utils.queue.TioFullWaitQueue;
import org.tio.utils.thread.pool.AbstractQueueRunnable;

import java.util.concurrent.Executor;

/**
 * @author WChao
 * @date 2018年4月3日 上午10:47:40
 */
public class MsgQueueRunnable extends AbstractQueueRunnable<Message> {
	
	private Logger log = LoggerFactory.getLogger(MsgQueueRunnable.class);
	
	private ImChannelContext imChannelContext;

	private ProtocolCmdProcessor protocolCmdProcessor;

	/** The msg queue. */
	private FullWaitQueue<Message> msgQueue = null;
	
	@Override
	public FullWaitQueue<Message> getMsgQueue() {
		if (msgQueue == null) {
			synchronized (this) {
				if (msgQueue == null) {
					msgQueue = new TioFullWaitQueue<Message>(Integer.MAX_VALUE, true);
				}
			}
		}
		return msgQueue;
	}

	public MsgQueueRunnable(ImChannelContext imChannelContext, Executor executor) {
		super(executor);
		this.imChannelContext = imChannelContext;
	}

	@Override
	public void runTask() {
		Message message;
		while ((message = this.getMsgQueue().poll()) != null) {
			if(protocolCmdProcessor != null){
				protocolCmdProcessor.process(imChannelContext, message);
			}
		}
	}

	public ProtocolCmdProcessor getProtocolCmdProcessor() {
		return protocolCmdProcessor;
	}

	public void setProtocolCmdProcessor(ProtocolCmdProcessor protocolCmdProcessor) {
		this.protocolCmdProcessor = protocolCmdProcessor;
	}
}
