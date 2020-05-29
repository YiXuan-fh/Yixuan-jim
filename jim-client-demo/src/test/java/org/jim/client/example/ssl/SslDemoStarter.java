package org.jim.client.example.ssl;

import java.nio.ByteBuffer;

import org.jim.client.ssl.NioSslClient;
import org.jim.core.packets.ChatBody;
import org.jim.core.packets.Command;
import org.jim.core.packets.LoginReqBody;
import org.jim.core.tcp.TcpPacket;
import org.jim.core.tcp.TcpServerEncoder;

public class SslDemoStarter {
	
	ServerRunnable serverRunnable;
	
	public SslDemoStarter() {
		serverRunnable = new ServerRunnable("TLSv1.2", "localhost", 9222,"./src/main/resources/keystore.jks","214323428310224");
		Thread server = new Thread(serverRunnable);
		server.start();
	}
	
	public void runDemo() throws Exception {
		
		NioSslClient client = new NioSslClient("TLSv1.2", "localhost", 9222,"./src/main/resources/keystore.jks","214323428310224");
		client.connect();
		byte[] loginBody = new LoginReqBody("ssl_hello_client","123").toByte();
		TcpPacket loginPacket = new TcpPacket(Command.COMMAND_LOGIN_REQ,loginBody);
		ByteBuffer loginByteBuffer = TcpServerEncoder.encode(loginPacket, null, null);
		client.write(loginByteBuffer.array());
		ChatBody chatBody =  ChatBody.newBuilder()
				.from("ssl_hello_client")
				.to("admin")
				.msgType(0)
				.chatType(1)
				.groupId("100")
				.content("SSL-Socket普通客户端消息测试!").build();
		TcpPacket chatPacket = new TcpPacket(Command.COMMAND_CHAT_REQ,chatBody.toByte());
		ByteBuffer chatByteBuffer = TcpServerEncoder.encode(chatPacket,null, null);
		client.write(chatByteBuffer.array());
		client.read();
		client.shutdown();

		/*NioSslClient client2 = new NioSslClient("TLSv1.2", "localhost", 9222);
		NioSslClient client3 = new NioSslClient("TLSv1.2", "localhost", 9222);
		NioSslClient client4 = new NioSslClient("TLSv1.2", "localhost", 9222);

		client2.connect();
		client2.write("Hello! I am another client!");
		client2.read();
		client2.shutdown();

		client3.connect();
		client4.connect();
		client3.write("Hello from client3!!!");
		client4.write("Hello from client4!!!");
		client3.read();
		client4.read();
		client3.shutdown();
		client4.shutdown();*/

		serverRunnable.stop();
	}
	
	public static void main(String[] args) throws Exception {
		SslDemoStarter demo = new SslDemoStarter();
		Thread.sleep(1000);	// Give the server some time to start.
		demo.runDemo();
	}
	
}
