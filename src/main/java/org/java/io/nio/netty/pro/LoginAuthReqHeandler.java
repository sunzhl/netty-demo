package org.java.io.nio.netty.pro;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class LoginAuthReqHeandler extends ChannelHandlerAdapter {
	private static final Logger LOG = Logger.getLogger(LoginAuthReqHeandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 连接成功后继续登录握手操作
		LOG.info("开始发起握手操作....");
		ctx.writeAndFlush(this.buildReqMessage());

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof NettyMessage) { // 如果是
			NettyMessage message = (NettyMessage) msg;
			if (null != message.getHeader() && message.getHeader().getType() == MessageType.LOGIN_RES.value()) { // 登录应答消息
				if (message.getBody() != null) {
					byte body = (byte) message.getBody();
					if (body != (byte) 0) { // 握手失败
						LOG.error("握手操作失败...");
						ctx.close();
					} else {
						LOG.info("登录握手成功..." + message);
						ctx.fireChannelRead(msg);
					}
				}
			}

		} else {
			ctx.fireChannelRead(msg);
		}
	}

	protected NettyMessage buildReqMessage() {

		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setSessionID(System.currentTimeMillis());
		header.setMsgLength(0);
		header.setPriority((byte) 0);
		header.setType(MessageType.LOGIN_REQ.value());
		message.setHeader(header);
		return message;

	}

}
