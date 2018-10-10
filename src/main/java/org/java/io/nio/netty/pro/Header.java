package org.java.io.nio.netty.pro;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息头信息定义类
 * 
 * @author admin
 *
 */
public final class Header {

	private int crcCode = 0xABEF0101; // 固定值

	private int msgLength; // 消息总长度包括消息头和消息体两部分的消息长度

	private long sessionID; // 会话ID

	private byte type; // 消息类型
	private byte priority; // 优先级

	private Map<String, Object> attachment = new HashMap<String, Object>(); // 附加信息

	public int getMsgLength() {
		return msgLength;
	}

	public void setMsgLength(int msgLength) {
		this.msgLength = msgLength;
	}

	public long getSessionID() {
		return sessionID;
	}

	public void setSessionID(long sessionID) {
		this.sessionID = sessionID;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getPriority() {
		return priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	public Map<String, Object> getAttachment() {
		return attachment;
	}

	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}

	public int getCrcCode() {
		return crcCode;
	}

	public void setCrcCode(int crcCode) {
		this.crcCode = crcCode;
	}

	public String toString() {
		return String.format("{msgLength: %d, type: %d, priority: %d, sessionID: %d}", this.msgLength, this.type,
				this.priority, this.sessionID);
	}

}
