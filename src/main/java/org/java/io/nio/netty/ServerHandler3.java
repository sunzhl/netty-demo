package org.java.io.nio.netty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class ServerHandler3 extends ChannelHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("我是第三个开始读取的handler-----");
		super.channelRead(ctx, msg);
	}
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println("我是开始写数据的第三个handler----");
		super.write(ctx, msg, promise);
	}
	
	
}
