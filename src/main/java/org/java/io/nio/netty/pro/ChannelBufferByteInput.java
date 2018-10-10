package org.java.io.nio.netty.pro;

import java.io.IOException;

import org.jboss.marshalling.ByteInput;

import io.netty.buffer.ByteBuf;

public class ChannelBufferByteInput implements ByteInput {

	private final ByteBuf buffer;

	public ChannelBufferByteInput(ByteBuf buffer) {
		this.buffer = buffer;
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public int read() throws IOException {
		if (buffer.isReadable()) {
			return buffer.readByte() & 0xFF;
		}
		return -1;
	}

	@Override
	public int read(byte[] b) throws IOException {
		return this.read(b, 0, b.length);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int readable = this.available();
		if(readable < 0) {
			return -1;
		}
		
		int length = Math.min(readable, len);
		this.buffer.readBytes(b, off, length);
		return length;
	}

	@Override
	public int available() throws IOException {
		return buffer.readableBytes();
	}

	@Override
	public long skip(long n) throws IOException {
		int readable = this.available();
		if(readable < n) {
			n = readable;
		}
		
		buffer.readerIndex((int)(buffer.readerIndex() + n));
		return n;
	}

}
