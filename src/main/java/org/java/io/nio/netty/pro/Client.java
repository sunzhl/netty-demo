package org.java.io.nio.netty.pro;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class Client {
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	public void connect(String host, int port) throws InterruptedException {

		EventLoopGroup group = new NioEventLoopGroup();

		Bootstrap bs = new Bootstrap();

		bs.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
						ch.pipeline().addLast(new NettyMessageEncoder());
						ch.pipeline().addLast(new ReadTimeoutHandler(50));
						ch.pipeline().addLast(new LoginAuthReqHeandler());
						ch.pipeline().addLast(new HeartBeatReqHandler());
					}
				});

		try {
			ChannelFuture future = bs.connect(host, port).sync();
			future.channel().closeFuture().sync();
		} finally {

			executor.execute(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(5000);
						try {
							connect(host, port);
						} catch (Exception e) {
							System.out.println("连接失败等待重连....");
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});

		}

	}
	
	
	public static void main(String[] args) {
		try {
			new Client().connect("localhost", 8080);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
