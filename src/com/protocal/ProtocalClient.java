package com.protocal;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/*
 * 客户端
 */
public class ProtocalClient {
	
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 7080;
	static long counter=0;	//记录
	final static int fil=100;	//发多少数据
	static long start=0;		
	
	public static void main(String[] args) {
		start=System.currentTimeMillis();	//获得当前时间
		IoConnector connector = new NioSocketConnector();	//客户端
		connector.getFilterChain().addLast("coderc",
				new ProtocolCodecFilter(new ProtocalFactory(Charset.forName("UTF-8")))); // 过滤器的编辑
		connector.getSessionConfig().setReadBufferSize(1024);	//客户端配置信息
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		connector.setHandler(new MyClientHandler());
		ConnectFuture connectFuture = connector.connect(new InetSocketAddress(HOST,PORT));//连接服务端，返回一个connectFuture
		connectFuture.addListener(new IoFutureListener<ConnectFuture>() {	//加入监听事件

			@Override
			public void operationComplete(ConnectFuture futrue) {
				if(futrue.isConnected()) {	//完成连接
					IoSession session = futrue.getSession();	//获得session会话
					sendata(session);		
				}
			}
			
		});
	}
	
	public static void sendata(IoSession session) {
		for(int i = 0; i < fil; i++) {
			String content = "luxiaobai:" + i;//数据
			ProtocalPack pack = new ProtocalPack((byte)i,content);//拼装数据
			session.write(pack);	//发送数据
			System.out.println("客户端发送数据：" + pack);
		}
	}
}
	
	//业务
	class MyClientHandler extends IoHandlerAdapter{

		@Override
		public void messageReceived(IoSession session, Object message) throws Exception {
			ProtocalPack pack = (ProtocalPack)message;
			System.out.println("client-->" + pack);
		}

		@Override
		public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
			if(status==IdleStatus.READER_IDLE) {//空闲时，进行关闭
				session.close(true);
			}
		}
		
	}