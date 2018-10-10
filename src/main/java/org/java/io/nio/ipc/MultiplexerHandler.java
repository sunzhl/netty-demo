package org.java.io.nio.ipc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.java.io.utils.Utils;

public class MultiplexerHandler implements Runnable {

	private static final int BUFFER_SIZE = 1024;
	private int port;

	private volatile boolean isStop = false;
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;

	public MultiplexerHandler(int port) {
		super();
		this.port = port;
		//init();
	}

	public void init() {

		try {
			this.selector = Selector.open();
			this.serverSocketChannel = ServerSocketChannel.open();
			this.serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
			this.serverSocketChannel.configureBlocking(false);
			this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			/*if (this.selector != null) {
				try {
					this.selector.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (null != this.serverSocketChannel) {
				try {
					this.serverSocketChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}*/
		}
	}

	@Override
	public void run() {
          init();
		while (!isStop) {

			try {
				int num = this.selector.select(1000);
				Set<SelectionKey> keys = this.selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					it.remove();
					this.iOHandler(key);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	protected void iOHandler(SelectionKey key) throws IOException {

		if (key.isValid()) {

			if (key.isAcceptable()) { // 有新连接接入
				System.out.println("---有新的连接接入----");
				ServerSocketChannel channel = (ServerSocketChannel) key.channel();
				SocketChannel sc = channel.accept();
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ); // 把SocketChannel注册到selector，并且监听读就绪事件

			}

			if (key.isReadable()) { // 读就绪
				System.out.println("-----状态可读----");
				SocketChannel sc = (SocketChannel) key.channel();
				ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
				int size = sc.read(buffer);
				if (size > 0) {
					buffer.flip(); // 把缓冲区buffer设置为写模式
					byte[] bytes = new byte[buffer.remaining()];
					buffer.get(bytes);
					String body = new String(bytes, "UTF-8");

					String result = Utils.getContent(body);
					this.doWriter(sc, result);
				}else if(size < 0) {
					System.err.println("--读取数据异常---");
					key.cancel();
					sc.close();
				}else {
					System.out.println("---没有读取到数据---");
				}
			}

		}

	}

	public void doWriter(SocketChannel sc, String content) throws IOException {

		if (StringUtils.isNotBlank(content)) {
			byte[] bytes = content.getBytes("UTF-8");
			ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
			buffer.put(bytes);
			buffer.flip();
			sc.write(buffer);
			this.setStop(true);
		}

	}

	public boolean isStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public static void main(String[] args) {
		new Thread(new MultiplexerHandler(8080)).start();
	}
	
}
