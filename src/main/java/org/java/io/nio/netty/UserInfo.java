package org.java.io.nio.netty;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class UserInfo implements Serializable {

	private static final long serialVersionUID = 547632504335349263L;

	private int userId;

	private String userName;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String toString() {
		return String.format("{id: %d, name: %s}", this.userId, this.userName);
	}

	public byte[] decode(ByteBuffer buffer) {

		byte[] bytes = this.userName.getBytes();

		buffer.putInt(bytes.length);
		buffer.put(bytes);
		buffer.putInt(userId);
		bytes = null;
		buffer.flip();
		byte[] value = new byte[buffer.remaining()];
		buffer.get(value);
		return value;
	}

}
