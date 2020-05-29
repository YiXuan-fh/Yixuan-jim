package org.jim.core.listener;

import org.jim.core.ImChannelContext;
import org.jim.core.exception.ImException;
import org.jim.core.packets.User;

/**
 * @ClassName ImUserListener
 * @Description 绑定/解绑用户监听器
 * @Author WChao
 * @Date 2020/1/12 14:24
 * @Version 1.0
 **/
public interface ImUserListener {
    /**
     * 绑定用户后回调该方法
     * @param imChannelContext IM通道上下文
     * @param user 绑定用户信息
     * @throws Exception
     * @author WChao
     */
    void onAfterBind(ImChannelContext imChannelContext, User user) throws ImException;

    /**
     * 解绑用户后回调该方法
     * @param imChannelContext IM通道上下文
     * @param user 解绑用户信息
     * @throws Exception
     * @author WChao
     */
    void onAfterUnbind(ImChannelContext imChannelContext, User user) throws ImException;
}
