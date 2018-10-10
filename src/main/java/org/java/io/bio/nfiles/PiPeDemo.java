package org.java.io.bio.nfiles;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PiPeDemo {

	private static  Pipe pipe;
	
	static {
		 try {
			pipe = Pipe.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	static class ReadThread implements Runnable{

		@Override
		public void run() {
			
			ByteBuffer buffer = ByteBuffer.allocate(128);
			try {
				//Pipe pipe = Pipe.open();
				
				 SourceChannel sChannel = pipe.source();
				 
				int count =  0;
				while(true) {
					count = sChannel.read(buffer);
					if(count == -1) {
						continue;
					}
					buffer.flip();
					byte[] bytes = buffer.array();
					System.out.println("输入：" + new String(bytes, 0, count));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	static class WriteThread implements Runnable{

		@Override
		public void run() {
			
			ByteBuffer buffer = ByteBuffer.allocate(512);
			
			try {
				//Pipe pipe = Pipe.open();
				
				
				SinkChannel sinkChannel =  pipe.sink();
				
				for(int i = 0; i < 10; i++) {
					Thread.sleep(1000);
					buffer.clear();
					String str = new Date().toLocaleString();
					System.out.println("输出：" + str);
					buffer.put(str.getBytes());
					buffer.flip();
					while(buffer.hasRemaining()) {
						sinkChannel.write(buffer);
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	public static void main(String[] args) {
		
		new Thread(new ReadThread()).start();
		new Thread(new WriteThread()).start();
		
		Path path = Paths.get("D:\\avro");
		
		try {
			
			FileSystems.getDefault();
			
			
			path.register(new WatchService() {
				
				@Override
				public WatchKey take() throws InterruptedException {
					return null;
				}
				
				@Override
				public WatchKey poll(long timeout, TimeUnit unit) throws InterruptedException {
					return null;
				}
				
				@Override
				public WatchKey poll() {
					return null;
				}
				
				@Override
				public void close() throws IOException {
					
				}
			}, StandardWatchEventKinds.ENTRY_CREATE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
}
