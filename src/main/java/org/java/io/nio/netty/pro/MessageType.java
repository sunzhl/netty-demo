package org.java.io.nio.netty.pro;

/**
 * Netty私有协议请求及相应类型
 * @author admin
 *
 */
public enum MessageType {
	
	//业务请求类型
	BUSI_REQ((byte)0),
	//业务响应类型
	BUSI_RES((byte)1),
	//业务ONE WAY 消息
	BUSI_ONE_WAY((byte)2),
	//登录认证请求消息
	LOGIN_REQ((byte)3),
    //登录认证响应消息	
	LOGIN_RES((byte)4),
	//心跳请求消息
	HEARTBEAT_REQ((byte)5),
	//心跳响应消息
	HEARTBATE_RES((byte)6);
	
	private byte value;
	
	private MessageType(byte value) {
		this.value = value;
	}
	
	public byte value() {
		return this.value;
	}
}
