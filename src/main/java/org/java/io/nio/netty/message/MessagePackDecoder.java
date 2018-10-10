package org.java.io.nio.netty.message;

import java.util.List;

import org.java.io.nio.netty.UserInfo;
import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MessagePackDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

		final byte[] array;
		final int len = msg.readableBytes();
		array = new byte[len];

		msg.getBytes(msg.readerIndex(), array, 0, len); // 从缓存字节中可读去的地方读取数据

		MessagePack mp = new MessagePack();
		mp.register(UserInfo.class);
		Object obj = mp.read(array, UserInfo.class);
		System.out.println("解码后的数据为：" + obj);
		out.add(obj);
	}

}
