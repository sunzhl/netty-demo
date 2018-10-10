package org.java.io.nio.ipc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOClientHandler implements Runnable {

	private static final String DEFAULT_HOST = "localhost";

	private static final int DEFAULT_PORT = 8080;
	private static final String ORDER = "QUERY_TIME_ORDER";
	private String host;
	private int port;

	private Selector selector;
	private SocketChannel socketChannel;

	private volatile boolean isStop = false;

	public NIOClientHandler() {
		this(DEFAULT_HOST, DEFAULT_PORT);
	}

	public NIOClientHandler(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void init() throws IOException {
		this.socketChannel = SocketChannel.open();
		this.selector = Selector.open();
		this.socketChannel.configureBlocking(false);
	}

	public void doConnect() throws IOException {
        System.out.println("----开始连接-----");
		if (this.socketChannel.connect(new InetSocketAddress(host, port))) { // 连接成功
			System.out.println("----连接成功-----");
			this.socketChannel.register(this.selector, SelectionKey.OP_READ); // 连接成功注册读操作事件
			this.doWriter(this.socketChannel); // 连接成功执行写操作
		} else {
			this.socketChannel.register(this.selector, SelectionKey.OP_CONNECT); // 连接失败注册连接操作事件
		}

	}

	public void doWriter(SocketChannel sc) {

		try {
			System.out.println("----开始发送信息----");
			byte[] bytes = ORDER.getBytes("UTF-8");
			ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
			buffer.put(bytes);
			buffer.flip();
			sc.write(buffer);
			if (buffer.hasRemaining()) {
				sc.write(buffer);
			} else {
				System.out.println("数据已发送完......");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {

		try {
			this.init();
			this.doConnect();
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (!isStop) {
			try {
				this.selector.select();

				Set<SelectionKey> keys = this.selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				SelectionKey key = null;
				while (it.hasNext()) {
					try {
						key = it.next();
						it.remove();
						this.doHandler(key);
					} catch (Exception e) {
						if (key != null) {
							key.cancel();
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void doHandler(SelectionKey key) throws IOException {

		if (key.isValid()) {
			SocketChannel channel = (SocketChannel) key.channel();
			if (key.isConnectable()) { // 可以连接判断是否连接成功
				if (channel.finishConnect()) {
					channel.register(selector, SelectionKey.OP_READ); //
					this.doWriter(channel);
				} else {
					System.exit(1); // 连接失败退出
				}
			}

			if (key.isReadable()) {
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				int size = channel.read(buffer);
				if (size > 0) {
					buffer.flip();
					byte[] bytes = new byte[buffer.remaining()];
					buffer.get(bytes);
					System.out.println("服务端返回的时间为：" + new String(bytes));
					this.stop();
				}

			}

		}

	}

	public void stop() {
		this.isStop = true;
	}
	
	
	public static void main(String[] args) {
		new Thread(new NIOClientHandler()).start();
	}
	

}
