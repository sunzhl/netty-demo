package org.java.io.nio.netty.pro;

import java.io.IOException;
import java.util.Map.Entry;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

	private MarshallingEncoder encoder;

	public NettyMessageEncoder() throws IOException {

		encoder = new MarshallingEncoder();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf buffer) throws Exception {

		if (msg == null || msg.getHeader() == null) {
			throw new NullPointerException("Message or message.header cond't null.");
		}

		buffer.writeInt(msg.getHeader().getCrcCode());
		buffer.writeInt(msg.getHeader().getMsgLength());
		buffer.writeLong(msg.getHeader().getSessionID());
		buffer.writeByte(msg.getHeader().getType());
		buffer.writeByte(msg.getHeader().getPriority());
		buffer.writeInt(msg.getHeader().getAttachment().size());

		String key = null;
		byte[] keyArray = null;
		Object obj = null;
		for (Entry<String, Object> entry : msg.getHeader().getAttachment().entrySet()) {

			key = entry.getKey();
			keyArray = key.getBytes(CharsetUtil.UTF_8);
			buffer.writeInt(keyArray.length);
			buffer.writeBytes(keyArray);
			obj = entry.getValue();
			this.encoder.encoder(obj, buffer);
		}
		key = null;
		keyArray = null;
		obj = null;

		if (null != msg.getBody()) {
			this.encoder.encoder(msg.getBody(), buffer);
		} else {
			buffer.writeInt(0);
		}
		buffer.setInt(4, buffer.readableBytes() - 8);

	}

}
