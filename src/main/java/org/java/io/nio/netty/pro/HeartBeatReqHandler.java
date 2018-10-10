package org.java.io.nio.netty.pro;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;

public class HeartBeatReqHandler extends ChannelHandlerAdapter {
	private static final Logger LOG = Logger.getLogger(HeartBeatReqHandler.class);
	private volatile ScheduledFuture<?> heartBeat;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof NettyMessage) {
			NettyMessage message = (NettyMessage) msg;
			if (null != message.getHeader() && message.getHeader().getType() == MessageType.LOGIN_RES.value()) {// 登录成功
				heartBeat = ctx.executor().scheduleAtFixedRate(
						new HeartBeatTask(ctx, message.getHeader().getSessionID()), 0, 5000, TimeUnit.MILLISECONDS);
			} else if (null != message.getHeader()
					&& message.getHeader().getType() == MessageType.HEARTBATE_RES.value()) {
				LOG.info("服务端返回心跳消息..." + message);
			}

		} else {
			ctx.fireChannelRead(msg);
		}
		
		ctx.channel().config().setAutoRead(true);
		
		ctx.write("");

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

		if (null != heartBeat) {
			heartBeat.cancel(true);
			heartBeat = null;
		}

		ctx.fireExceptionCaught(cause);

	}

	private class HeartBeatTask implements Runnable {

		private ChannelHandlerContext ctx;
		private Long sid;

		public HeartBeatTask(ChannelHandlerContext ctx, Long sid) {
			this.ctx = ctx;
			this.sid = sid;
		}

		@Override
		public void run() {

			NettyMessage message = this.bulidMessage();
			LOG.info(String.format("客户端[%s]发送心跳检测....", message));
			ctx.writeAndFlush(message);
		}

		private NettyMessage bulidMessage() {
			NettyMessage message = new NettyMessage();
			Header header = new Header();
			header.setType(MessageType.HEARTBEAT_REQ.value());
			header.setSessionID(sid);
			message.setHeader(header);
			return message;
		}

	}

}
