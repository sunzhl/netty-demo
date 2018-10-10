package netty_stu.org.netty.stu;

import org.apache.log4j.Logger;
import org.java.io.nio.netty.pro.MessageType;

/**
 * Hello world!
 *
 */
public class App {
	static Logger LOGGER = Logger.getLogger(App.class);
	public static void main(String[] args) {
		System.out.println("Hello World!");
		
	    LOGGER.info("测试Log4j");
	    
	    LOGGER.info(MessageType.BUSI_ONE_WAY.value());
	}
}
