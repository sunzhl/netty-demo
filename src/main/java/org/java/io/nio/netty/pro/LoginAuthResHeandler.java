package org.java.io.nio.netty.pro;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class LoginAuthResHeandler extends ChannelHandlerAdapter {

	private static final Logger LOG = Logger.getLogger(LoginAuthResHeandler.class);

	private static final Map<String, Boolean> NODE_CACHE = new HashMap<String, Boolean>();

	private static final String[] WORK_LIST = new String[] { "127.0.0.1", "192.168.24.1" };

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       LOG.info("请求登录握手.." + msg);
		if (msg instanceof NettyMessage) {
			NettyMessage message = (NettyMessage) msg;
			if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()) { // 如果是登录握手
				String nodeIndex = ctx.channel().remoteAddress().toString();
				NettyMessage logRes = null;
				Long sid = message.getHeader().getSessionID();
				if (NODE_CACHE.containsKey(nodeIndex)) { // 客户端已经登录过，不允许重复登录
					logRes = this.buildMessage((byte) -1, sid);
				} else {
					InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
					String ip = address.getAddress().getHostAddress();
					boolean isOk = false;
					for (String str : WORK_LIST) { // 判断是否在白名单中
						if (str.equals(ip)) {
							isOk = true;
							break;
						}
					}

					if (isOk) {
						logRes = this.buildMessage((byte) 0, sid);
						NODE_CACHE.put(nodeIndex, true);
					} else {
						logRes = this.buildMessage((byte) -1, sid);
					}
					LOG.info("握手响应数据为：" + logRes);
					ctx.writeAndFlush(logRes);
				}

			} else {
				ctx.fireChannelRead(msg);
			}

		} else {
			ctx.fireChannelRead(msg);
		}

	}

	protected NettyMessage buildMessage(byte result, Long sessionId) {

		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_RES.value());
		header.setSessionID(sessionId);
		message.setBody(result);
		message.setHeader(header);
		return message;

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		NODE_CACHE.remove(ctx.channel().remoteAddress().toString());
		ctx.close();
		ctx.fireExceptionCaught(cause);

	}

}
