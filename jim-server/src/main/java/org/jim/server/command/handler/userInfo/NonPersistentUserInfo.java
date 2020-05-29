package org.jim.server.command.handler.userInfo;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.jim.core.ImChannelContext;
import org.jim.core.packets.Group;
import org.jim.core.packets.User;
import org.jim.core.packets.UserReqBody;
import org.jim.core.packets.UserStatusType;
import org.jim.server.util.ImServerKit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 非持久化获取用户信息处理
 */
public class NonPersistentUserInfo implements IUserInfo {
    /**
     * 好友分组标志
     */
    private static int FRIEND_GROUP_FLAG = 0;
    /**
     * 群组分组标志
     */
    private static int GROUP_FLAG = 1;

    @Override
    public User getUserInfo(UserReqBody userReqBody, ImChannelContext imChannelContext) {
        User user = imChannelContext.getSessionContext().getImClientNode().getUser();
        Integer type = userReqBody.getType();
        if(Objects.isNull(user)) {
            return null;
        }
        User cloneUser = user.clone();
        //在线用户;
        if(UserStatusType.ONLINE.getNumber() == type || UserStatusType.OFFLINE.getNumber() == type){
            //处理好友分组在线用户相关信息;
            List<Group> onlineFriends = initOnlineUserFriendsGroups(user.getFriends(), type,FRIEND_GROUP_FLAG);
            if(onlineFriends != null){
                cloneUser.setFriends(onlineFriends);
            }
            //处理群组在线用户相关信息;
            List<Group> onlineGroups = initOnlineUserFriendsGroups(user.getGroups(), type,GROUP_FLAG);
            if(onlineGroups != null){
                cloneUser.setGroups(onlineGroups);
            }
            return cloneUser;
            //所有用户(在线+离线);
        }else if(UserStatusType.ALL.getNumber() == type){
            return user;
        }
        return user;
    }

    /**
     * 处理在线用户好友及群组用户;
     * @param groups 用户群组集合
     * @param flag(0：好友,1:群组)
     * @return
     */
    private static List<Group> initOnlineUserFriendsGroups(List<Group> groups, Integer type, Integer flag){
        if(CollectionUtils.isEmpty(groups)) {
            return Lists.newArrayList();
        }
        //处理好友分组在线用户相关信息;
        List<Group> resultGroups = new ArrayList<Group>();
        groups.forEach(group -> {
            Group cloneGroup = group.clone();
            List<User> users = null;
            if(FRIEND_GROUP_FLAG == flag){
                users = group.getUsers();
            }else if(GROUP_FLAG == flag){
                users = ImServerKit.getUsersByGroup(group.getGroupId());
            }
            resultGroups.add(cloneGroup);
            if(CollectionUtils.isEmpty(users))return;
            List<User> cloneUsers = new ArrayList<User>();
            users.forEach(user -> {
                User onlineUser = ImServerKit.getUser(user.getUserId());
                //在线
                if(onlineUser != null && UserStatusType.ONLINE.getNumber() == type){
                    cloneUsers.add(onlineUser.clone());
                //离线
                }else if(onlineUser == null && UserStatusType.OFFLINE.getNumber() == type){
                    cloneUsers.add(onlineUser.clone());
                }
            });
            cloneGroup.setUsers(cloneUsers);
        });
        return resultGroups;
    }

}
