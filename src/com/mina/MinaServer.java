package com.mina;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/*
 * mina服务端 
 */
public class MinaServer {
	
	static int PORT=7080;	//端口
	static IoAcceptor accept=null;
	
	public static void main(String[] args) {
		try {
			
		accept = new NioSocketAcceptor();
		//设置编码过滤器
		accept.getFilterChain().addLast("codec", new ProtocolCodecFilter(
				new TextLineCodecFactory(
						Charset.forName("UTF-8"),
						LineDelimiter.WINDOWS.getValue(),
						LineDelimiter.WINDOWS.getValue())));
		accept.getFilterChain().addFirst("filter", new MyServerFilter());
		
		
		accept.getSessionConfig().setReadBufferSize(1024);	//连接配置信息
		accept.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		accept.setHandler(new MyServerhandler());
		accept.bind(new InetSocketAddress(PORT));	//绑定端口
		System.out.println("Server ->" + PORT);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
