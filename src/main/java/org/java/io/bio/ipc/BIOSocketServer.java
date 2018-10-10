package org.java.io.bio.ipc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOSocketServer {

	private static final String DEFAULT_HOST_NAME = "localhost";
	private static final int DEFAULT_PORT = 8080;
	private String hostName;
	private int port;
	private volatile boolean isStop = false;

	private ServerSocket serverSocket;

	public BIOSocketServer() {
		this(DEFAULT_HOST_NAME, DEFAULT_PORT);
	}

	public BIOSocketServer(String hostName, int port) {
		super();
		this.hostName = hostName;
		this.port = port;
	}

	public void init() {

		try {
			System.out.println("启动Socket服务端,监听端口为：" + this.port);
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		this.init();
		while (!isStop) {

			try {
				Socket socket = this.serverSocket.accept();
				System.out.println("获取到客户端请求,地址为：" + socket.getInetAddress().getHostAddress());
				new Thread(new SocketHandler(socket)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public void close() {
		if (this.serverSocket != null) {
			try {
				this.serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		
		new BIOSocketServer().start();
		
	}
	

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

}
