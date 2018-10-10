package org.java.io.nio.netty.ws;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

	private WebSocketServerHandshaker handshaker;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端新的请求接入：" + ctx.channel().remoteAddress().toString());
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {

		// 第一次握手是有HTTP协议承载,所以他是一个HTTP请求，有他来建立握手
		if (msg instanceof FullHttpRequest) {
			this.handHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			this.webSocketHandler(ctx, (WebSocketFrame) msg);
		}
	}

	public void webSocketHandler(ChannelHandlerContext ctx, WebSocketFrame wsf) {

		System.out.println(String.format("WebSocket请求的类型为：%s ...", wsf.getClass().getName()));

		if (wsf instanceof CloseWebSocketFrame) { // 关闭链接
			System.out.println("请求关闭链接.....");
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) wsf.retain());
			return;
		}

		if (wsf instanceof PingWebSocketFrame) { // ping请求
			System.out.println("Ping 操作....");
			ctx.channel().write(new PongWebSocketFrame(wsf.content().retain()));
			return;
		}

		if (!(wsf instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(String.format("%s is not supported.", wsf.getClass().getName()));
		}

		String request = ((TextWebSocketFrame) wsf).text();

		System.out.println("接受到客戶段的請求為：" + request);
		for (int i = 0; i < 1000; i++) {
			String currentTime = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(new Date());
			ctx.channel().writeAndFlush(new TextWebSocketFrame("歡迎使用Netty WebSocket,當前時刻為：" + currentTime));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void handHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {

		// 如果解码失败, 发送失败消息 。并且判断此次请求是否为 WebSocket请求 。从头信息中获取 Upgrade=websocket
//		if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
//			sendHttpResponse(ctx, req,
//					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST)); // 非法请求
//			return;
//		}

		System.out.println("开始创建握手......");

		//ctx.attr(AttributeKey.valueOf("")).get();

		// 首先创建一个握手工厂
		WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket",
				null, false);

		// 创建握手处理类
		handshaker = factory.newHandshaker(req);

		if (null == handshaker) {
			System.out.println("创建握手操作失败.....");
//			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			System.out.println("创建握手操作成功.....");
			handshaker.handshake(ctx.channel(), req); // 构造握手消息并返回给客户端
		}

	}

	/**
	 * 
	 * @param ctx
	 * @param req
	 * @param res
	 */
	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {

//		if (res.status().code() != 200) {
//
//			ByteBuf buffer = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
//			res.content().writeBytes(buffer);
//			buffer.release();
//			HttpHeaderUtil.setContentLength(res, res.content().readableBytes());
//		}
//		ChannelFuture f = ctx.channel().writeAndFlush(res);
//		if (!HttpHeaderUtil.isKeepAlive(req) || res.status().code() != 200) {
//			f.addListener(ChannelFutureListener.CLOSE); // 关闭连接
//		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

}
