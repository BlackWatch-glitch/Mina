package com.mina;

import java.util.Date;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class MyServerhandler extends IoHandlerAdapter{

	//连接出现异常的时候调用的方法
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		System.out.println("exceptionCaught");
	}
	
	//接收到数据
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String msg = (String)message;	//读的数据
		System.out.println("服务端接受到数据：" + msg);
//		if(msg.equals("exit")) {
//			session.close();
//		}
		Date date = new Date();	//发一个时间到客户端
		session.write(date);	//发数据到客户端
	}

	//发送数据
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		System.out.println("messageSent");
		session.close();	//长连接变成短连接
	}
	
	//session关闭的时候调用的方法
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("sessionClosed");
	}
	
	//session创建的时候调用的方法
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("sessionCreated");
	}
	
	//多少时间处于空闲状态
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		System.out.println("sessionIdle");
	}
	
	//打开session
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("sessionOpened");
	}
	
}
