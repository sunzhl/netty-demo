package org.java.io.nio.netty.message;

import java.nio.charset.Charset;

import org.java.io.nio.netty.UserInfo;
import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

public class MessagePackEncoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		MessagePack mp = new MessagePack();
		mp.register(UserInfo.class);
		byte[] bytes = "{\"name\": \"张三\"}\n".getBytes(CharsetUtil.UTF_8); //mp.write(msg);
        System.out.println("编码数据为：" + msg);
		out.writeBytes(bytes);
	}

}
