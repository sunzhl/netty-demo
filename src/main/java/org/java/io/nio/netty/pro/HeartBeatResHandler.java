package org.java.io.nio.netty.pro;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class HeartBeatResHandler extends ChannelHandlerAdapter {
	private static final Logger LOG = Logger.getLogger(HeartBeatResHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof NettyMessage) {
			NettyMessage message = (NettyMessage) msg;
			if (null != message.getHeader() && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {// 心跳请求
				LOG.info(String.format("收到客户端[%s]的心跳消息..", message));
				ctx.writeAndFlush(this.bulidMessage(message.getHeader().getSessionID()));
			}

		} else {
			ctx.fireChannelRead(msg);
		}

	}

	private NettyMessage bulidMessage(Long sid) {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.HEARTBATE_RES.value());
		header.setSessionID(sid);
		message.setHeader(header);
		return message;
	}
}
