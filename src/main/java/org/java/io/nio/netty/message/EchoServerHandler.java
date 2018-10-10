package org.java.io.nio.netty.message;

import java.util.List;

import org.java.io.nio.netty.UserInfo;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("服务端出现异常：" + cause.getMessage());
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务端获取的数据为：" + msg);
		if (msg instanceof UserInfo) {
			UserInfo info = (UserInfo) msg;
			info.setUserName(info.getUserName() + "--server");
			ctx.writeAndFlush(info);
		}else if(msg instanceof List){
			List<UserInfo> list = (List<UserInfo>) msg;
			list.stream().map(u -> u.getUserName() + "-server").forEach(u -> ctx.writeAndFlush(u));
		} else {
			System.out.println("server get message: " + msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

}
