package org.jim.server.listener;

import org.jim.core.ImChannelContext;
import org.jim.core.listener.ImListener;

/**
 * @ClassName ImServerListener
 * @Description IM服务端连接监听器接口
 * @Author WChao
 * @Date 2020/1/4 9:35
 * @Version 1.0
 **/
public interface ImServerListener extends ImListener {
    /**
     *
     * 服务器检查到心跳超时时，会调用这个函数（一般场景，该方法只需要直接返回false即可）
     * @param imChannelContext
     * @param interval 已经多久没有收发消息了，单位：毫秒
     * @param heartbeatTimeoutCount 心跳超时次数，第一次超时此值是1，以此类推。此值被保存在：channelContext.stat.heartbeatTimeoutCount
     * @return 返回true，那么服务器则不关闭此连接；返回false，服务器将按心跳超时关闭该连接
     */
     boolean onHeartbeatTimeout(ImChannelContext imChannelContext, Long interval, int heartbeatTimeoutCount);
}
