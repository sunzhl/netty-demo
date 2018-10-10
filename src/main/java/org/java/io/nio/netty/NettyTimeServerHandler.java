package org.java.io.nio.netty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.java.io.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;
import sun.security.krb5.internal.CredentialsUtil;

public class NettyTimeServerHandler extends ChannelHandlerAdapter {
	
	private static final BlockingQueue<String> QUEUE = new ArrayBlockingQueue<String>(20);
	
	private boolean flag = true;
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("服务端出现异常：" + cause.getMessage());
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		String body = new String(bytes, CharsetUtil.UTF_8);
		System.out.println("接收到的消息：" + body);
		QUEUE.add(body);
		
		System.out.println("当前队列中的数据个数为：" + QUEUE.size());
		
		if(QUEUE.size() > 3) { //如果队列中的命令大于10个，限流处理
			System.out.println("客户端发送的请求已经超出了我能处理的能力，暂时限流处理----");
			ctx.channel().config().setAutoRead(false);
		}
		new Thread(new MyThread(ctx)).start();
		
//		QUEUE.forEach(str -> {
//		});
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//ctx.flush(); //把缓冲区中的数据刷入到通道中
	}

	class MyThread implements Runnable{
		
		private ChannelHandlerContext ctx;
		
		public MyThread(ChannelHandlerContext ctx) {
			this.ctx = ctx;
		}
		
		@Override
		public void run() {
			if(flag) {
				try {
					Thread.sleep(10 * 1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				flag = false;
			}
			while(true) {
				while(QUEUE.size() > 0) {
					String order = QUEUE.poll();
					/*String currentTime = "QUERY_TIME_ORDER".equalsIgnoreCase(order) ? new Date(System.nanoTime()).toString()
							: "BAD_ORDER";
					ByteBuf buffer = Unpooled.copiedBuffer(currentTime.getBytes(CharsetUtil.UTF_8));
//			ctx.write(buffer); //把数据发暂时写入到缓冲中
					ctx.writeAndFlush(buffer);*/
					try {
						Thread.sleep(5 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				boolean autoRead = ctx.channel().config().isAutoRead();
				if(!autoRead) {
					System.out.println("处理完成可以继续接受数据");
					ctx.channel().config().setAutoRead(true); //处理完成后又可以接受数据
				}
			}
		}
	}
}


