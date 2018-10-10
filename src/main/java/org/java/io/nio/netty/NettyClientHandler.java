package org.java.io.nio.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClientHandler {

	public void connect(String host, int port) {

		EventLoopGroup group = new NioEventLoopGroup();

		Bootstrap bs = new Bootstrap();

		bs.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new NettyTimeClientHandler()); //把自定义的ChannelHandler注册到ChannelPipeline中
					}
				});

		try {
			ChannelFuture future = bs.connect(host, port).sync(); //创建连接
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new NettyClientHandler().connect("localhost", 8080);
	}

}
