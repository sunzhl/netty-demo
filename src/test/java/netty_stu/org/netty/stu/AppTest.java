package netty_stu.org.netty.stu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.java.io.nio.netty.UserInfo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	
	
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		
		/*ByteArrayOutputStream aos = null;
		ObjectOutputStream oos = null;
		
		UserInfo user = new UserInfo();
		user.setUserId(10);
		user.setUserName("张三");
		
		try {
			aos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(aos);
			
			oos.writeObject(user);
			oos.flush();
			oos.close();
			byte[] bytes = aos.toByteArray();
			
			System.out.println("Java自带序列化后的大小：" + bytes.length);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		System.out.println("使用二进制序列化的大小：" + user.decode(buffer).length);*/
		
		
		ByteBuf buf = Unpooled.buffer(20);
		
		System.out.println("readindex: " + buf.readerIndex());
		System.out.println("writerindex: " + buf.writerIndex());
		
		buf.writeInt(2); //一个整数站4个字节

		System.out.println(Arrays.toString(buf.array()));
		System.out.println("readindex: " + buf.readerIndex());
		System.out.println("writerindex: " + buf.writerIndex());
		
		int pos = buf.writerIndex();
		buf.writeBytes(new byte[4]);
		byte[] src = new byte[] {'A', 'B', 'C', 'D'};
		buf.writeBytes(src);
		System.out.println(Arrays.toString(buf.array()));
		
		System.out.println("写入的长度为：" + (buf.writerIndex() - pos - 4));
		
		buf.setInt(pos, buf.writerIndex() - pos - 4);
		buf.writeByte(0x00);
		System.out.println(Arrays.toString(buf.array()));
		
		buf.clear();
		
		buf.writeBytes("ABCDEF".getBytes());
		
		System.out.println("buf->writerIndex = " + buf.writerIndex());
		System.out.println("buf->readerIndex = " + buf.readerIndex());
		System.out.println("buf->capacity = " + buf.capacity());
		
		buf.setInt(0, 10);
		
		System.out.println("buf->writerIndex = " + buf.writerIndex());
		System.out.println("buf->readerIndex = " + buf.readerIndex());
		
		ByteBuf buf1 = Unpooled.copiedBuffer(buf);
		
		System.out.println("buf1->capacity = " + buf1.capacity());
		System.out.println("buf1->readerIndex = " + buf1.readerIndex());
		System.out.println("buf1->writerIndex = " + buf1.writerIndex());
		
		buf1.readBytes(10);
		buf1.readInt();
	}
	
	
	public void testStr() {
		
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		
		
		String str = "pmedmitjtckhxwhvpwemznhmhzhpueainchqrftkmbjlradhmjekcqzansyzkvqhwnrdgzdbzewdmxkzrscikdaugbvygntrifnolehdtrqjlasofuvzeijbmzehkxknmjekcxswqldknysfsxrqaqzp";
		
		String[] strs = str.split("");
		Set<String> set = new HashSet<String>();
		
		List<String> list = Arrays.asList(strs);
		
		list.stream();
		
		for(String s: strs) {
			if(map.containsKey(s)) {
				System.out.println("第一个出现重复的字符为：" + s);
				map = null;
				break;
			}else {
				map.put(s, true);
			}
		}
		
		for(int i = 512; i > 0; i <<= 1) {
			System.out.println(i);
		}
		
		System.out.println(1073741824 * 2);
	}
	
	
}
