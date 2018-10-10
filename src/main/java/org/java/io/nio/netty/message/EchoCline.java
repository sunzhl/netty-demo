package org.java.io.nio.netty.message;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoCline {

	public void connect(String host, int port) {

		EventLoopGroup group = new NioEventLoopGroup();

		Bootstrap b = new Bootstrap();

		b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
		 .handler(new LoggingHandler(LogLevel.INFO))
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
//						ch.pipeline().addLast(new LengthFieldPrepender(3)); //在编码之前对信息头添加两个字节信息
						ch.pipeline().addLast(new MessagePackEncoder());
//						ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2));
						ch.pipeline().addLast(new LineBasedFrameDecoder(18));
						ch.pipeline().addLast(new ByteToObjMessageDecoder());
						ch.pipeline().addLast(new EchoClientHandler());
					}
				});

		try {
			ChannelFuture f = b.connect(host, port).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}

	}
	
	public static void main(String[] args) {
		new EchoCline().connect("localhost", 8080);
	}

}
