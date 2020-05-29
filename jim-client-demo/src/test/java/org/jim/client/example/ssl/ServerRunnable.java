package org.jim.client.example.ssl;

import org.jim.client.ssl.NioSslServer;

/**
 * This class provides a runnable that can be used to initialize a {@link NioSslServer} thread.
 * <p/>
 * Run starts the server, which will start listening to the configured IP address and port for
 * new SSL/TLS connections and serve the ones already connected to it.
 * <p/>
 * Also a stop method is provided in order to gracefully close the server and stop the thread.
 * 
 * @author <a href="mailto:alex.a.karnezis@gmail.com">Alex Karnezis</a>
 */
public class ServerRunnable implements Runnable {

	NioSslServer server;
	public  ServerRunnable(String protocol, String hostAddress, int port, String keyStorePath , String keyStorePassword) {
		try {
			server = new NioSslServer(protocol,hostAddress, port,keyStorePath,keyStorePassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Should be called in order to gracefully stop the server.
	 */
	public void stop() {
		server.stop();
	}
	
}
