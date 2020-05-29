package org.jim.core;

import org.jim.core.config.ImConfig;
import org.tio.core.ChannelContext;
import org.tio.core.Node;
import org.tio.utils.prop.MapWithLockPropSupport;

/**
 * @ClassName ImChannelContext
 * @Description TODO
 * @Author WChao
 * @Date 2020/1/5 23:40
 * @Version 1.0
 **/
public abstract class ImChannelContext extends MapWithLockPropSupport implements ImConst{

    protected ImConfig imConfig;

    protected ChannelContext tioChannelContext;

    public ImChannelContext(ImConfig imConfig, ChannelContext tioChannelContext) {
        this.imConfig = imConfig;
        this.tioChannelContext = tioChannelContext;
    }

    public ImConfig getImConfig() {
        return imConfig;
    }

    public void setImConfig(ImConfig imConfig) {
        this.imConfig = imConfig;
    }

    public void setSessionContext(ImSessionContext imSessionContext){
        this.setAttribute(Key.IM_CHANNEL_SESSION_CONTEXT_KEY, imSessionContext);
    }

    public ImSessionContext getSessionContext(){
        return (ImSessionContext)this.getAttribute(Key.IM_CHANNEL_SESSION_CONTEXT_KEY);
    }

    public String getId() {
        return tioChannelContext.getId();
    }

    public Node getClientNode(){
        return tioChannelContext.getClientNode();
    }

    public void setPacketNeededLength(Integer packetNeededLength) {
        this.tioChannelContext.setPacketNeededLength(packetNeededLength);
    }

    public String getUserId(){
        return tioChannelContext.userid;
    }

    public void setUserId(String userId){
        tioChannelContext.setUserid(userId);
    }

    public ChannelContext getTioChannelContext() {
        return tioChannelContext;
    }

}
