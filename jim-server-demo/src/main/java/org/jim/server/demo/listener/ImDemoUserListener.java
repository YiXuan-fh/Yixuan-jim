package org.jim.server.demo.listener;

import com.alibaba.fastjson.JSONObject;
import org.jim.core.ImChannelContext;
import org.jim.core.exception.ImException;
import org.jim.core.packets.User;
import org.jim.server.listener.AbstractImUserListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WChao
 * @Desc
 * @date 2020-05-02 18:18
 */
public class ImDemoUserListener extends AbstractImUserListener {

    private static Logger logger = LoggerFactory.getLogger(ImDemoUserListener.class);

    @Override
    public void doAfterBind(ImChannelContext imChannelContext, User user) throws ImException {
        logger.info("绑定用户:{}", JSONObject.toJSONString(user));
    }

    @Override
    public void doAfterUnbind(ImChannelContext imChannelContext, User user) throws ImException {
        logger.info("解绑用户:{}",JSONObject.toJSONString(user));
    }
}
