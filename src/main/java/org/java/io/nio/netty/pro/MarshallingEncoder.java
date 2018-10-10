package org.java.io.nio.netty.pro;

import java.io.IOException;

import org.jboss.marshalling.Marshaller;

import io.netty.buffer.ByteBuf;

public class MarshallingEncoder {

	private static final byte[] LENGHT_PLACEHOLDER = new byte[4];

	private Marshaller marshalling;

	public MarshallingEncoder() throws IOException {
		marshalling = MarshallingCodecFactory.bulidMarshalling();
	}

	public void encoder(Object msg, ByteBuf out) throws IOException {

		try {
			int lengthPos = out.writerIndex(); // 获取可写入数据的位置

			out.writeBytes(LENGHT_PLACEHOLDER); // 写如4个字节的展位符
			ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);

			marshalling.start(output);
			marshalling.writeObject(msg); //
			marshalling.flush();
			out.setInt(lengthPos, out.writerIndex() - lengthPos - 4); // 设置写入body的长度
		} finally {
			marshalling.close();
		}
	}
}
