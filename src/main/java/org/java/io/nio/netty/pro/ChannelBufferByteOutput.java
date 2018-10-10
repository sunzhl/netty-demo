package org.java.io.nio.netty.pro;

import java.io.IOException;

import org.jboss.marshalling.ByteOutput;

import io.netty.buffer.ByteBuf;

public class ChannelBufferByteOutput implements ByteOutput {

	private final ByteBuf buffer;

	public ChannelBufferByteOutput(ByteBuf buffer) {
		this.buffer = buffer;
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public void flush() throws IOException {

	}

	@Override
	public void write(int b) throws IOException {
		buffer.writeInt(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		buffer.writeBytes(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		buffer.writeBytes(b, off, len);
	}

	public ByteBuf getBuffer() {
		return this.buffer;
	}

}
