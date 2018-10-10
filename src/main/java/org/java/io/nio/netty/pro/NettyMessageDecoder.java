package org.java.io.nio.netty.pro;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

	private MarshallingDecoder decoder;

	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		this.decoder = new MarshallingDecoder();
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf buffer = (ByteBuf) super.decode(ctx, in);

		if (null == buffer || buffer.readableBytes() <= 0) {
			return null;
		}

		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setCrcCode(buffer.readInt());
		header.setMsgLength(buffer.readInt());
		header.setSessionID(buffer.readLong());
		header.setType(buffer.readByte());
		header.setPriority(buffer.readByte());

		int size = buffer.readInt();
		if (size > 0) {
			Map<String, Object> att = new HashMap<String, Object>(size);
			int keyLen = 0;
			String key = null;
			byte[] keyArray = null;
			for (int i = 0; i < size; i++) {
				keyLen = buffer.readInt();
				keyArray = new byte[keyLen];
				key = new String(keyArray, CharsetUtil.UTF_8);
				att.put(key, this.decoder.decoder(buffer));
			}
			key = null;
			keyArray = null;
			header.setAttachment(att);
		}
		message.setHeader(header);
		if (buffer.readableBytes() > 4) {
			message.setBody(this.decoder.decoder(buffer));
		}

		return message;
	}

}
