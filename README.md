[![输开源协议](https://img.shields.io/badge/License-Apache--2.0-brightgreen.svg "Apache")](https://www.apache.org/licenses/LICENSE-2.0)

[![maven最新版本](https://maven-badges.herokuapp.com/maven-central/org.j-im/jim-core/badge.svg "maven最新版本")](https://maven-badges.herokuapp.com/maven-central/org.j-im/jim-core)
## J-IM简介

 J-IM 是用JAVA语言,基于t-io开发的轻量、高性能、单机支持几十万至百万在线用户IM，主要目标降低即时通讯门槛，快速打造低成本接入在线IM系统，通过极简洁的消息格式就可以实现多端不同协议间的消息发送如内置(Http、Websocket、Tcp自定义IM协议)等，并提供通过http协议的api接口进行消息发送无需关心接收端属于什么协议，一个消息格式搞定一切！  

## 官方网站

 http://www.j-im.cn
                                                                     
## 主要特点
        1、高性能(单机可支持几十万至百万人同时在线)
        2、轻量、可扩展性极强
        3、支持集群多机部署
        4、支持SSL/TLS加密传输
        5、消息格式极其简洁(JSON)
        6、一端口支持可插拔多种协议(Socket自定义IM协议、Websocket、Http),各协议可分别独立部署。
        7、内置消息持久化(离线、历史、漫游)，保证消息可靠性，高性能存储
        8、各种丰富的API接口。
        9、零成本部署，一键启动。

## 消息格式

 **1.聊天请求消息结构** 
 ```
{
    "from": "来源ID",
    "to": "目标ID",
    "cmd":"命令码(11)int类型",
    "createTime": "消息创建时间long类型",
    "msgType": "消息类型int类型(0:text、1:image、2:voice、3:vedio、4:music、5:news)",
    "chatType":"聊天类型int类型(0:未知,1:公聊,2:私聊)",
    "groupId":"群组id仅在chatType为(1)时需要,String类型",
    "content": "内容",
    "extras" : "扩展字段,JSON对象格式如：{'扩展字段名称':'扩展字段value'}"
}
```
请求:COMMAND_CHAT_REQ(11) 响应:COMMAND_CHAT_RESP(12)

 **2.鉴权请求消息结构** 
```
{
    "cmd":"命令码(3)int类型",
    "token": "校验码"
}
```
请求:COMMAND_AUTH_REQ(3) 响应:COMMAND_AUTH_RESP(4)

 **3.握手请求消息结构** 
```
{
    "cmd":"命令码(1)int类型",
    "hbyte":"握手1个字节"
}
```
说明:请求:COMMAND_HANDSHAKE_REQ(1) 响应:COMMAND_HANDSHAKE_RESP(2)

 **4.登录请求消息结构** 
```
{
    "cmd":"命令码(5)int类型",
    "userId": "用户账号",
    "password": "密码",
    "token": "校验码(此字段可与userId、password共存,也可只选一种方式)"
}
```
请求:COMMAND_LOGIN_REQ(5) 响应:COMMAND_LOGIN_RESP(6)

 **5.心跳请求消息结构** 
```
{
    "cmd":"命令码(13)int类型",
    "hbbyte":"心跳1个字节"
}
```
请求:COMMAND_HEARTBEAT_REQ(13) 响应:COMMAND_HEARTBEAT_REQ(13)

 **6.关闭、退出请求消息结构** 
```
{
    "cmd":"命令码(14)int类型",
    "userId":"用户ID"
}
```
请求:COMMAND_CLOSE_REQ(14) 响应:无

 **7.获取用户信息请求消息结构** 
```
{
     "cmd":"命令码(17)int类型",
     "userId":"用户id(必填项)",
     "type":"获取类型(0:所有在线用户,1:所有离线线用户,2:所有用户[在线+离线])"
}
```
请求:COMMAND_GET_USER_REQ(17) 响应:COMMAND_GET_USER_RESP(18)

**8.获取持久化聊天消息(离线+历史+漫游)请求结构** 
```
{
     "cmd":"命令码(19)int类型",
     "fromUserId":"消息发送用户id(此字段必须与userId一起使用,获取双方聊天消息),非必填",
     "userId":"当前用户id(必填字段),当只有此字段时,type必须为0，意思是获取当前用户所有离线消息(好友+群组)",
     "groupId":"群组id(此字段必须与userId一起使用,获取当前用户指定群组聊天消息),非必填",
     "beginTime":"消息区间开始时间Date毫秒数double类型,非必填",
     "endTime":"消息区间结束时间Date毫秒数double类型,非必填",
     "offset":"分页偏移量int类型，类似Limit 0,10 中的0,非必填",
     "count":"显示消息数量,类似Limit 0,10 中的10,非必填",
     "type":"消息类型(0:离线消息,1:历史消息)"
}
```
请求:COMMAND_GET_MESSAGE_REQ(19) 响应:COMMAND_GET_MESSAGE_RESP(20)

## 使用

- 服务端(快速开发自己的高性能IM服务器)：引入jim-server,在你的pom.xml中加入如下代码片段
```
<dependency>
    <groupId>org.j-im</groupId>
    <artifactId>jim-server</artifactId>
    <version>3.0.0.v20200501-RELEASE</version>
</dependency>
```

- 客户端(快速开发自己的IM客户端)：引入jim-client,在你的pom.xml中加入如下代码片段
```
<dependency>
    <groupId>org.j-im</groupId>
    <artifactId>jim-client</artifactId>
    <version>3.0.0.v20200501-RELEASE</version>
</dependency>
```

## J-IM新版官网截图
![输入图片说明](https://images.gitee.com/uploads/images/2020/0413/041753_1561ab18_410355.png "屏幕截图.png")

![输入图片说明](https://images.gitee.com/uploads/images/2020/0413/041959_f9d41209_410355.png "屏幕截图.png")

![输入图片说明](https://images.gitee.com/uploads/images/2020/0413/041930_9b8ee6dc_410355.png "屏幕截图.png")

![输入图片说明](https://images.gitee.com/uploads/images/2020/0413/041841_e7a8a173_410355.png "屏幕截图.png")

## J-IM老版一些截图

  Web访问地址:http://localhost:8888

  ![输入图片说明](https://gitee.com/uploads/images/2017/0922/195539_4a5d9ed4_410355.jpeg "tio-img-0.jpg")

  Http协议api调用地址:http://localhost:8888/api/message/send

  ![输入图片说明](https://gitee.com/uploads/images/2017/0830/190054_a128b214_410355.jpeg "tio-im-2.jpg")

  ![输入图片说明](https://gitee.com/uploads/images/2017/0830/190428_474270ae_410355.jpeg "tio-im-3.jpg")

## 近期发布
- 支持Https、Wss  **(已完成)** 
- 支持J-PUSH消息推送
- 支持图片、语音、视频发送等
- 官网开发

## 鸣谢
[t-io不仅仅百万并发框架](https://gitee.com/tywo45/t-io)

## 联系方式
   个人QQ:1241503759<br>
   个人Email:wchaojava@163.com

   官方QQ群号：245513673 <a target="_blank"  href="//shang.qq.com/wpa/qunwpa?idkey=c876012ca4131bfdb6a32c9e5b4cde730b686fa1d20cc2ebd795a0fe2760b5d8"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="J-IM朋友圈" title="J-IM朋友圈"></a><br>

### 重点说明： 申请进群时提供的是码云star显示的名字(列如下图)，管理员会挨个审核是否已star，如果没有star或者乱填的直接拒绝，出现拒绝2次的永久不允许进群：
![码云名称说明](https://images.gitee.com/uploads/images/2020/0408/160035_caf78b5d_410355.png "屏幕截图.png")
![群二维码](https://gitee.com/uploads/images/2018/0415/131601_e2cf7bde_410355.png "1523769300727.png")