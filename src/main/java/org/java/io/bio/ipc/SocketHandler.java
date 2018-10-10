package org.java.io.bio.ipc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 客户端请求的Socket的处理类
 * 
 * @author admin
 *
 */
public class SocketHandler implements Runnable {

	private Socket socket;

	public SocketHandler(Socket socket) {
		this.socket = socket;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {

		BufferedReader reader = null;
		PrintWriter writer = null;

		try {
			reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			writer = new PrintWriter(this.socket.getOutputStream());

			String line = null;

			while (true) {
				line = reader.readLine();
				if (null == line) {
					break;
				}
				System.out.println("The time server receive order is '" + line + "'");
				line = StringUtils.deleteWhitespace(line);
				String currentTime = "QUERY_TIME_ORDER".equalsIgnoreCase(line) ? new Date(System.nanoTime()).toString()
						: "BAD_ORDER";

				writer.println(currentTime + "\n");
				writer.flush();

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(writer);
			IOUtils.closeQuietly(this.socket);
		}

	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
