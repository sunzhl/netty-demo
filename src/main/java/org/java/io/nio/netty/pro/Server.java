package org.java.io.nio.netty.pro;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class Server {

	public void bind(int port) {

		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup workers = new NioEventLoopGroup();

		ServerBootstrap sb = new ServerBootstrap();

		sb.group(boss, workers).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {

						ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
						ch.pipeline().addLast(new NettyMessageEncoder());
						ch.pipeline().addLast(new ReadTimeoutHandler(50));
						ch.pipeline().addLast(new LoginAuthResHeandler());
						ch.pipeline().addLast(new HeartBeatResHandler());

					}
				});

		try {
			ChannelFuture future = sb.bind(port).sync();

			future.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			workers.shutdownGracefully();
		}

	}

	public static void main(String[] args) {
		new Server().bind(8080);
	}
}
