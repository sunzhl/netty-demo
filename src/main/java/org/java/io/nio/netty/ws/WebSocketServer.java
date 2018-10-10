package org.java.io.nio.netty.ws;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {

	public void bind(int port) {

		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup workers = new NioEventLoopGroup();

		ServerBootstrap sbs = new ServerBootstrap();

		sbs.group(boss, workers).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
				.handler(new LoggingHandler(LogLevel.DEBUG)).childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// 添加解码器，把请求或响应的消息解码或编码为HTTP消息
						ch.pipeline().addLast("http-coder", new HttpServerCodec());
						// 把HTTP消息的多个部分组合为一个完整的HTTP消息
						ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
						// 来向客户端发送HTML5文件，主要用于支持服务端和浏览器端的WebSocket消息通信
						ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
						// 自定义的WebSocket处理类
						ch.pipeline().addLast("handler", new WebSocketServerHandler());
//						ch.pipeline().addLast(group, handlers)

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
	    new WebSocketServer().bind(8080);	
	}

}
