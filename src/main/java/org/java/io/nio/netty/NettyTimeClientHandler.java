package org.java.io.nio.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

public class NettyTimeClientHandler extends ChannelHandlerAdapter {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println("客户端出现异常：" + cause.getMessage());
		ctx.close();

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		while(true) {
			byte[] bytes = "QUERY_TIME_ORDER".getBytes(CharsetUtil.UTF_8);
			System.out.println("开始发送信息......");
			ByteBuf buffer = Unpooled.copiedBuffer(bytes);
			ctx.writeAndFlush(buffer); //向缓冲区中写入数据，同时刷新缓冲区数据到I/O通道
			Thread.sleep(1000L);
		}

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buffer = (ByteBuf) msg;
		byte[] bytes = new byte[buffer.readableBytes()];
		buffer.readBytes(bytes);
		System.out.println("返回的信息为：" + new String(bytes, CharsetUtil.UTF_8));
	}

}
