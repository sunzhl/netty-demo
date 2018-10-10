package org.java.io.nio.netty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class ServerHandler2 extends ChannelHandlerAdapter{

	static int times = 0;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		System.out.println("我是第二个开始读取的Handler-----");
		ctx.fireChannelRead(msg);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println("我是开始写数据的第二个handler----");
		if(times == 0) {
			++times;
			System.out.println("================重新开始写===========");
			ctx.channel().write(msg); //如果冲Channel开始写，数据将从ChannelPipeline链的头开始执行
		}else {
			super.write(ctx, msg, promise); //每个ChannelHandler绑定了一个ChannelHandlerContext，如果调用 ChannelHandlerContext开始调用write方法
			                                //将从当前ChannelHandler开始执行
		}
	}
	
	
}
