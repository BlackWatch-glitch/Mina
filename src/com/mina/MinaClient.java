package com.mina;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaClient {
	
	private static String host="127.0.0.1";
	private static int port=7080;
	
	public static void main(String[] args) {
		
		//获得会话连接
		IoSession session = null;
		IoConnector connector = new NioSocketConnector();
		connector.setConnectTimeout(3000);
		//设置过滤器
		connector.getFilterChain().addLast("coderc",
				new ProtocolCodecFilter(new TextLineCodecFactory(
						Charset.forName("UTF-8"),
						LineDelimiter.WINDOWS.getValue(),
						LineDelimiter.WINDOWS.getValue())));
		
		connector.getFilterChain().addFirst("filter", new MyClientFilter());
		
		connector.setHandler(new MyClientHandler());
		
		ConnectFuture futrue = connector.connect(new InetSocketAddress(host,port));	//连接服务端
		futrue.awaitUninterruptibly();	//等待我们的连接
		session = futrue.getSession();
		session.write("你好！世界");
		session.getCloseFuture().awaitUninterruptibly();	//等待关闭连接
		connector.dispose();	//关闭时进行清理操作
	}
}
