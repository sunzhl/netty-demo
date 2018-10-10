package org.java.io.nio.netty.message;

import java.util.ArrayList;
import java.util.List;

import org.java.io.nio.netty.UserInfo;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoClientHandler extends ChannelHandlerAdapter {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		List<UserInfo> infos = this.getUserInfos();
        System.out.println("开始写数据.....");
		infos.forEach(u -> ctx.writeAndFlush(u));
        System.out.println("写数据完成...");
//		ctx.flush();
		System.out.println("刷新数据完成....");

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		System.out.println("client get message " + msg);

	}

	public List<UserInfo> getUserInfos() {

		List<UserInfo> infos = new ArrayList<UserInfo>();
		for (int i = 0; i < 10; i++) {
			UserInfo info = new UserInfo();
			info.setUserId(i);
			info.setUserName("messagePack -> " + i);
			infos.add(info); // info;
		}

		return infos;
	}

}
