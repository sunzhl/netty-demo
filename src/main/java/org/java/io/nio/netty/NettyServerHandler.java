package org.java.io.nio.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

public class NettyServerHandler {

	public void bind(int port) {

		EventLoopGroup boss = new NioEventLoopGroup(); //创建主线程组
		EventLoopGroup workers = new NioEventLoopGroup(); //创建工作线程主

		ServerBootstrap sb = new ServerBootstrap(); //创建服务端引导

		sb.group(boss, workers).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new FixedLengthFrameDecoder(16));
						ch.pipeline().addLast(new NettyTimeServerHandler()); //把自定义的ChannelHandler注册到ChannelPipeline中
					}
				});
		try {
			ChannelFuture future = sb.bind(port).sync(); //绑定端口
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			workers.shutdownGracefully();
		}

	}

	public static void main(String[] args) {
		new NettyServerHandler().bind(8080);
	}

}
