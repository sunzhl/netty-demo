package org.java.io.nio.netty.message;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {

	public void bind(int port) {

		EventLoopGroup boss = new NioEventLoopGroup();

		EventLoopGroup workers = new NioEventLoopGroup();

		ServerBootstrap sbs = new ServerBootstrap();

		sbs.group(boss, workers).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
		        .handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
//						ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2));
						ch.pipeline().addLast(new LineBasedFrameDecoder(18));
						ch.pipeline().addLast(new ByteToObjMessageDecoder()); //配置解码器
//						ch.pipeline().addLast(new LengthFieldPrepender(2)); //在编码之前对信息头添加两个字节信息
						ch.pipeline().addLast(new MessagePackEncoder()); //配置编码器
						ch.pipeline().addLast(new EchoServerHandler());
					}
				});

		try {
			ChannelFuture f = sbs.bind(port).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			workers.shutdownGracefully();
		}

	}

	public static void main(String[] args) {
		new EchoServer().bind(8080);
	}

}
