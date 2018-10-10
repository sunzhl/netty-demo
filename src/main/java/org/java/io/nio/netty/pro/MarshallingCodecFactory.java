package org.java.io.nio.netty.pro;

import java.io.IOException;

import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;

public class MarshallingCodecFactory {

	/**
	 * 构建JBoss 的Marshalling的编码器
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Marshaller bulidMarshalling() throws IOException {

		// 通过工具类Marshalling构建 serial 类型的序列化工厂，serial 标示的是 java的序列化
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("river");

		// 配置文件设置版本为5
		final MarshallingConfiguration configuration = new MarshallingConfiguration();

		configuration.setVersion(4);

		return marshallerFactory.createMarshaller(configuration);
	}

	public static Unmarshaller bulidUnmarshaller() throws IOException {
		// 通过工具类Marshalling构建 serial 类型的序列化工厂，serial 标示的是 java的序列化
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("river");

		// 配置文件设置版本为5
		final MarshallingConfiguration configuration = new MarshallingConfiguration();

		configuration.setVersion(4);

		return marshallerFactory.createUnmarshaller(configuration);

	}

}
