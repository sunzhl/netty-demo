package org.java.io.nio.netty.message;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ByteToObjMessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("服务端接收到了数据");
		int readableBytes = in.readableBytes();
		if (readableBytes > 0) {
			byte[] bytes = new byte[readableBytes];
			in.readBytes(bytes);
			String str = new String(bytes, 0, readableBytes);
			out.add(str);
			System.out.println("读入的数据为：" + new String(bytes, "UTF-8"));
		}
	}

}
