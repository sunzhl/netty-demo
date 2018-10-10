package org.java.io.bio.ipc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;

public class BIOClient {

	private String host;
	private int port;

	public BIOClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	@SuppressWarnings("deprecation")
	public void run() {

		Socket socket = null;
		BufferedReader reader = null;
		PrintWriter writer = null;

		try {
			socket = new Socket(host, port);

			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream());

			writer.write("QUERY_TIME_ORDER\n");
			writer.flush();

			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println("服務端返回的時間為：" + line);
				break;
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(writer);
			IOUtils.closeQuietly(socket);
		}

	}
	
	public static void main(String[] args) {
		new BIOClient("localhost", 8080).run();
	}

}
