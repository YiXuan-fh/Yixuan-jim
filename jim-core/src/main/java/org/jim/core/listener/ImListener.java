package org.jim.core.listener;

import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;

/**
 * @ClassName ImListener
 * @Description IM连接监听器
 * @Author WChao
 * @Date 2020/1/4 11:09
 * @Version 1.0
 **/
public interface ImListener {
    /**
     * 建链后触发本方法，注：建链不一定成功，需要关注参数isConnected
     * @param imChannelContext
     * @param isConnected 是否连接成功,true:表示连接成功，false:表示连接失败
     * @param isReconnect 是否是重连, true: 表示这是重新连接，false: 表示这是第一次连接
     * @throws Exception
     * @author: WChao
     */
     void onAfterConnected(ImChannelContext imChannelContext, boolean isConnected, boolean isReconnect) throws Exception;

    /**
     * 原方法名：onAfterDecoded
     * 解码成功后触发本方法
     * @param imChannelContext
     * @param packet
     * @param packetSize
     * @throws Exception
     * @author: WChao
     */
     void onAfterDecoded(ImChannelContext imChannelContext, ImPacket packet, int packetSize) throws Exception;

    /**
     * 接收到TCP层传过来的数据后
     * @param imChannelContext
     * @param receivedBytes 本次接收了多少字节
     * @throws Exception
     */
     void onAfterReceivedBytes(ImChannelContext imChannelContext, int receivedBytes) throws Exception;

    /**
     * 消息包发送之后触发本方法
     * @param imChannelContext
     * @param packet
     * @param isSentSuccess true:发送成功，false:发送失败
     * @throws Exception
     * @author WChao
     */
     void onAfterSent(ImChannelContext imChannelContext, ImPacket packet, boolean isSentSuccess) throws Exception;

    /**
     * 处理一个消息包后
     * @param imChannelContext
     * @param packet
     * @param cost 本次处理消息耗时，单位：毫秒
     * @throws Exception
     */
     void onAfterHandled(ImChannelContext imChannelContext, ImPacket packet, long cost) throws Exception;

    /**
     * 连接关闭前触发本方法
     * @param imChannelContext
     * @param throwable the throwable 有可能为空
     * @param remark the remark 有可能为空
     * @param isRemove
     * @author WChao
     * @throws Exception
     */
     void onBeforeClose(ImChannelContext imChannelContext, Throwable throwable, String remark, boolean isRemove) throws Exception;

}
