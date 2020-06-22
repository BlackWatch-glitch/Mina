package com.protocal;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/*
 * 服务端
 */
public class ProtocalServer {

	private static final int port = 7080;

	public static void main(String[] args) throws IOException {

		IoAcceptor acceptor = new NioSocketAcceptor();	
		acceptor.getFilterChain().addLast("coderc",
				new ProtocolCodecFilter(new ProtocalFactory(Charset.forName("UTF-8")))); // 过滤器的编辑
		acceptor.getSessionConfig().setReadBufferSize(1024);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);//设置空闲时间
		acceptor.setHandler(new MyServerHandler());	//设置业务对象
		acceptor.bind(new InetSocketAddress(port));//绑定端口
		System.out.println("server start.....");
	}
}

class MyServerHandler extends IoHandlerAdapter {
	
	//发送异常
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		System.out.println("server->exceptionCaught");
	}

	//接收数据
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		ProtocalPack pack = (ProtocalPack) message;
		System.out.println("服务端接受：" + pack);
	}

	//空闲等待
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		System.out.println("server->sessionIdle");
	}

}
