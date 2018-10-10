package org.java.io.bio.nfiles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

import org.apache.commons.lang3.StringUtils;

public class BioFile {

	private final static int HEADER_BUFFER_SIZE = 4;
	private final static int BODY_BUFFER_SIZE = 10;

	public static void writToFile(FileChannel channel, ByteBuffer buffer) throws IOException {

		if (null != channel && buffer != null) {

			while (buffer.hasRemaining()) {
				channel.write(buffer);
			}

		}

	}

	public static void writerFile(String header, String body) {

		if (StringUtils.isBlank(header) || header.getBytes().length != HEADER_BUFFER_SIZE) {
			throw new IllegalArgumentException("The arguments [header] is error.");
		}

		ByteBuffer headBuffer = ByteBuffer.allocate(HEADER_BUFFER_SIZE);

		ByteBuffer bodyBuffer = ByteBuffer.allocate(BODY_BUFFER_SIZE);
		try {
			RandomAccessFile raf = new RandomAccessFile("D:\\avro\\nio.txt", "rw");

			FileChannel fChannel = raf.getChannel();
			headBuffer.put(header.getBytes("UTF-8"));
			headBuffer.flip();
			writToFile(fChannel, headBuffer);
			if (!StringUtils.isBlank(body)) {
				byte[] bytes = body.getBytes("UTF-8");

//				bodyBuffer.clear();
//				bodyBuffer.put(bytes);
//				bodyBuffer.flip();
//				writToFile(fChannel, bodyBuffer);

				int offset = 0, length = bytes.length;

				// if (length <= BODY_BUFFER_SIZE) {
				// bodyBuffer.put(bytes, 0, length);
				// bodyBuffer.flip();
				// fChannel.write(new ByteBuffer[] { headBuffer, bodyBuffer });
				// } else {
//					bodyBuffer.put(bytes, 0, BODY_BUFFER_SIZE);
//					bodyBuffer.flip();
//					fChannel.write(new ByteBuffer[] { headBuffer, bodyBuffer });
					int remain = length;
					while (offset < length) {
						int len = Math.min(remain, BODY_BUFFER_SIZE);
						bodyBuffer.clear();
						bodyBuffer.put(bytes, offset, len);
						bodyBuffer.flip();
						writToFile(fChannel, bodyBuffer);
						remain -= len;
						offset += len;
					}


				fChannel.force(true);
				fChannel.close();
				raf.close();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void readFile() {

		try {
			RandomAccessFile raf = new RandomAccessFile("D:\\avro\\User.avsc", "rw");

			FileChannel fChannel = raf.getChannel();

			ByteBuffer buffer = ByteBuffer.allocate(125);

			int byteLen = fChannel.read(buffer);

			/*
			 * while(buffer.hasRemaining()) { System.out.println((char) buffer.get()); }
			 */

			while (byteLen != -1) {
				buffer.flip();
				if (buffer.hasRemaining()) {
					System.out.println(new String(buffer.array(), 0, byteLen));
				}

				buffer.clear();
				byteLen = fChannel.read(buffer);
			}

			raf.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void bufferTest() {

		CharBuffer buffer = CharBuffer.allocate(100);

		buffer.put(new char[] { 'a', 'b', 'c', 'd' });

		System.out.println("position: " + buffer.position());
		System.out.println("limit: " + buffer.limit());

		buffer.flip();

		System.out.println("position: " + buffer.position());
		System.out.println("limit: " + buffer.limit());

		System.out.println("0--" + buffer.get());
		System.out.println("1--" + buffer.get());

		System.out.println("position: " + buffer.position());
		System.out.println("limit: " + buffer.limit());

		// buffer.clear();

		buffer.compact();

		System.out.println("position: " + buffer.position());
		System.out.println("limit: " + buffer.limit());

		buffer.compareTo(null);

		buffer.equals(buffer);
	}

	public static void transFromTo(String src, String dist) {
		RandomAccessFile srcFile = null;
		RandomAccessFile distFile = null;
		try {
			srcFile = new RandomAccessFile(src, "rw");

			distFile = new RandomAccessFile(dist, "rw");

			FileChannel fromChannel = srcFile.getChannel();
			FileChannel toChannel = distFile.getChannel();

			int position = 0;
			long count = fromChannel.size();

			toChannel.transferFrom(fromChannel, position, 10);
			toChannel.close();
			fromChannel.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

	}

	public static void main(String[] args) {
		// readFile();
		// bufferTest();

		String body = "Specifies a repository mirror site to use instead of a given repository. The repository that";
		String head = String.format("%04d", body.getBytes().length);
		System.out.println(head);
		writerFile(head, body);

		// transFromTo("D:\\avro\\nio.txt", "D:\\avro\\to.txt");

	}

}
