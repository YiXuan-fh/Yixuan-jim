package org.jim.server.helper.mongo;

import org.jim.core.listener.ImStoreBindListener;
import org.jim.core.message.MessageHelper;
import org.jim.core.packets.ChatBody;
import org.jim.core.packets.Group;
import org.jim.core.packets.User;
import org.jim.core.packets.UserMessageData;

import java.util.List;

/**
 * @author WChao
 * @Desc Mongo获取持久化+同步消息助手;
 * @date 2020-05-03 21:04
 */
public class MongoMessageHelper implements MessageHelper {
    @Override
    public ImStoreBindListener getBindListener() {
        return null;
    }

    @Override
    public boolean isOnline(String userId) {
        return false;
    }

    @Override
    public Group getGroupUsers(String group_id, Integer type) {
        return null;
    }

    @Override
    public List<Group> getAllGroupUsers(String user_id, Integer type) {
        return null;
    }

    @Override
    public Group getFriendUsers(String user_id, String friend_group_id, Integer type) {
        return null;
    }

    @Override
    public List<Group> getAllFriendUsers(String user_id, Integer type) {
        return null;
    }

    @Override
    public User getUserByType(String userid, Integer type) {
        return null;
    }

    @Override
    public void addGroupUser(String userid, String group_id) {

    }

    @Override
    public List<String> getGroupUsers(String group_id) {
        return null;
    }

    @Override
    public List<String> getGroups(String user_id) {
        return null;
    }

    @Override
    public void writeMessage(String timelineTable, String timelineId, ChatBody chatBody) {

    }

    @Override
    public void removeGroupUser(String userId, String group_id) {

    }

    @Override
    public UserMessageData getFriendsOfflineMessage(String userId, String fromUserId) {
        return null;
    }

    @Override
    public UserMessageData getFriendsOfflineMessage(String userId) {
        return null;
    }

    @Override
    public UserMessageData getGroupOfflineMessage(String userId, String groupId) {
        return null;
    }

    @Override
    public UserMessageData getFriendHistoryMessage(String userId, String fromUserId, Double beginTime, Double endTime, Integer offset, Integer count) {
        return null;
    }

    @Override
    public UserMessageData getGroupHistoryMessage(String userId, String groupId, Double beginTime, Double endTime, Integer offset, Integer count) {
        return null;
    }

    @Override
    public boolean updateUserTerminal(User user) {
        return false;
    }
}
