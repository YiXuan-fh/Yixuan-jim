/**
 * 
 */
package org.jim.server.demo.service;

import cn.hutool.core.util.RandomUtil;
import org.jim.core.*;
import org.jim.core.packets.*;
import org.jim.core.session.id.impl.UUIDSessionIdGenerator;
import org.jim.core.utils.Md5;
import org.jim.server.processor.login.LoginCmdProcessor;
import org.jim.server.protocol.AbstractProtocolCmdProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author WChao
 *
 */
public class LoginServiceProcessor extends AbstractProtocolCmdProcessor implements LoginCmdProcessor {

	private Logger logger = LoggerFactory.getLogger(LoginServiceProcessor.class);

	public static final Map<String, User> tokenMap = new HashMap<>();

	private static String[] familyName = new String[] { "J-", "刘", "张", "李", "胡", "沈", "朱", "钱", "王", "伍", "赵", "孙", "吕", "马", "秦", "毛", "成", "梅", "黄", "郭", "杨", "季", "童", "习", "郑",
			"吴", "周", "蒋", "卫", "尤", "何", "魏", "章", "郎", " 唐", "汤", "苗", "孔", "鲁", "韦", "任", "袁", "贺", "狄朱" };

	private static String[] secondName = new String[] { "艺昕", "红薯", "明远", "天蓬", "三丰", "德华", "歌", "佳", "乐", "天", "燕子", "子牛", "海", "燕", "花", "娟", "冰冰", "丽娅", "大为", "无为", "渔民", "大赋",
			"明", "远平", "克弱", "亦菲", "靓颖", "富城", "岳", "先觉", "牛", "阿狗", "阿猫", "辰", "蝴蝶", "文化", "冲之", "悟空", "行者", "悟净", "悟能", "观", "音", "乐天", "耀扬", "伊健", "炅", "娜", "春花", "秋香", "春香",
			"大为", "如来", "佛祖", "科比", "罗斯", "詹姆屎", "科神", "科蜜", "库里", "卡特", "麦迪", "乔丹", "魔术师", "加索尔", "法码尔", "南斯", "伊哥", "杜兰特", "保罗", "杭州", "爱湘", "湘湘", "昕", "函", "鬼谷子", "膑", "荡",
			"子家", "德利优视", "五方会谈", "来电话了","轨迹","超"};
	
	/**
	 * 根据用户名和密码获取用户
	 * @param loginReqBody
	 * @param imChannelContext
	 * @return
	 * @author: WChao
	 */
	public User getUser(LoginReqBody loginReqBody, ImChannelContext imChannelContext) {
		String text = loginReqBody.getUserId()+loginReqBody.getPassword();
		String key = ImConst.AUTH_KEY;
		String token = Md5.sign(text, key, CHARSET);
		User user = getUser(token);
		user.setUserId(loginReqBody.getUserId());
		return user;
	}
	/**
	 * 根据token获取用户信息
	 * @param token
	 * @return
	 * @author: WChao
	 */
	public User getUser(String token) {
		//demo中用map，生产环境需要用cache
		User user = tokenMap.get(token);
		if(Objects.nonNull(user)){
			return user;
		}
		User.Builder builder = User.newBuilder()
				.userId(UUIDSessionIdGenerator.instance.sessionId(null))
				.nick(familyName[RandomUtil.randomInt(0, familyName.length - 1)] + secondName[RandomUtil.randomInt(0, secondName.length - 1)])
				//模拟的群组,正式根据业务去查数据库或者缓存;
				.addGroup(Group.newBuilder().groupId("100").name("J-IM朋友圈").build());
		 //模拟的用户好友,正式根据业务去查数据库或者缓存;
		initFriends(builder);
		builder.avatar(nextImg()).status(UserStatusType.ONLINE.getStatus());
		user = builder.build();
		if (tokenMap.size() > 10000) {
			tokenMap.clear();
		}
		tokenMap.put(token, user);
		return user;
	}

	public void initFriends(User.Builder builder){
		Group myFriend = Group.newBuilder().groupId("1").name("我的好友").build();
		List<User> myFriendGroupUsers = new ArrayList();
		User user1 = User.newBuilder()
					 .userId(UUIDSessionIdGenerator.instance.sessionId(null))
					 .nick(familyName[RandomUtil.randomInt(0, familyName.length - 1)] + secondName[RandomUtil.randomInt(0, secondName.length - 1)])
					 .avatar(nextImg())
					 .build();
		myFriendGroupUsers.add(user1);
		myFriend.setUsers(myFriendGroupUsers);
		builder.addFriend(myFriend);
	}
	
	public String nextImg() {
		return ImgMnService.nextImg();
	}

	/**
	 * 登陆成功返回状态码:ImStatus.C10007
	 * 登录失败返回状态码:ImStatus.C10008
	 * 注意：只要返回非成功状态码(ImStatus.C10007),其他状态码均为失败,此时用户可以自定义返回状态码，定义返回前端失败信息
	 */
	@Override
	public LoginRespBody doLogin(LoginReqBody loginReqBody, ImChannelContext imChannelContext) {
		if(Objects.nonNull(loginReqBody.getUserId()) && Objects.nonNull(loginReqBody.getPassword())){
			return LoginRespBody.success();
		}else {
			return LoginRespBody.failed();
		}
	}

	@Override
	public void onSuccess(User user, ImChannelContext channelContext) {
		logger.info("登录成功回调方法");
	}

	@Override
	public void onFailed(ImChannelContext imChannelContext) {
		logger.info("登录失败回调方法");
	}
}
