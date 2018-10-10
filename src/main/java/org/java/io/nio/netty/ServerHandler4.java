package org.java.io.nio.netty;

import org.java.io.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;

public class ServerHandler4 extends ChannelHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("我是第四个开始读取的handler----");
//		super.channelRead(ctx, msg);
		
		ByteBuf buf = (ByteBuf) msg;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		String body = new String(bytes, CharsetUtil.UTF_8);
		System.out.println("接收到的消息：" + body);
		String currentTime = Utils.getContent(body) + Utils.LIMIT;
		ByteBuf buffer = Unpooled.copiedBuffer(currentTime.getBytes(CharsetUtil.UTF_8));
		ctx.write(buffer);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println("我是开始写数据的第四个handler----");
		super.write(ctx, msg, promise);
	}

	
}
