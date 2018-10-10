package org.java.io.nio.netty.pro;

import java.io.IOException;

import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import io.netty.buffer.ByteBuf;

public class MarshallingDecoder {

	private Unmarshaller unmarshaller;

	public MarshallingDecoder() throws IOException {
		unmarshaller = MarshallingCodecFactory.bulidUnmarshaller();
	}

	public Object decoder(ByteBuf buffer) throws IOException, ClassNotFoundException {

		try {
			int objSize = buffer.readInt(); // 读取body数据的长度
			ByteBuf buf = buffer.slice(buffer.readerIndex(), objSize);
			ByteInput input = new ChannelBufferByteInput(buf);

			unmarshaller.start(input);
			Object obj = unmarshaller.readObject();
			unmarshaller.finish();
			buffer.readerIndex(buffer.readerIndex() + objSize);
			return obj;
		} finally {
			unmarshaller.close();
		}
	}
}
