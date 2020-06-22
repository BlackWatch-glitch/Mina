package com.protocal;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/*
 * 解码器
 */
public class ProtocalDecoder implements ProtocolDecoder{
	
	private final AttributeKey CONTEXT = new AttributeKey(this.getClass(), "context");	//CONTEXT在session中保存我们的上下文，传入当前的对象this.getClass()
	private final Charset charset;
	private int maxPackLength = 100;	//包的最大长度
	
	public int getMaxPackLength() {
		return maxPackLength;
	}

	public void setMaxPackLength(int maxPackLength) {
		if(maxPackLength < 0) {
			throw new IllegalArgumentException("maxPackLength参数：" + maxPackLength);
		}
		this.maxPackLength = maxPackLength;
	}

	public ProtocalDecoder(Charset charset) {
		this.charset = charset;
	}
	
	public ProtocalDecoder() {
		this(Charset.defaultCharset());	//传入charset
	}
	
	
	public Context getContext(IoSession session) {
		Context ctx = (Context)session.getAttribute(CONTEXT);
		if(ctx==null) {
			ctx = new Context();
			session.setAttribute(CONTEXT,ctx);
		}
		return ctx;
	}
	
	//解码
	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		final int packHeadlength = 5;		//包头的长度
		Context ctx = this.getContext(session);	//获得session的上下文
		ctx.append(in);	//加入到上下文中
		IoBuffer buf = ctx.getBuf();	
		buf.flip();	//准备开始读取数据
		while(buf.remaining() >= packHeadlength) {	//查看是否有数据  buf.remaining缓冲区数据
			buf.mark();		//标记
			int length = buf.getInt(); //获得length
			byte flag = buf.get();	
			if(length < 0 || length > maxPackLength) { //length超出或许小于则是无效的
				buf.reset();	//重置
				break;
			}else if(length >= packHeadlength && length - packHeadlength <= buf.remaining()) {
				int oldLimit = buf.limit();	
				buf.limit(buf.position() + length - packHeadlength);	//content
				String content = buf.getString(ctx.getDecoder());	//取出content
				buf.limit(oldLimit);	
				ProtocalPack pakeage = new ProtocalPack(flag,content);
				out.write(pakeage);//写入
			}else {	//半包
				buf.clear();
				break;
			}
		}
		if(buf.hasRemaining()) {
			IoBuffer temp = IoBuffer.allocate(maxPackLength).setAutoExpand(true);
			temp.put(buf);
			temp.flip();
			buf.reset();
			buf.put(temp);
		}else {
			buf.reset();
		}
	}

	@Override
	public void dispose(IoSession session) throws Exception {//移除
		Context ctx = (Context)session.getAttribute(CONTEXT);
		if(ctx!=null) {
			session.removeAttribute(CONTEXT);
		}
		
	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	//内部类
	private class Context{
		private final CharsetDecoder decoder;
		private IoBuffer buf;
		private Context() {
			decoder = charset.newDecoder();
			buf = IoBuffer.allocate(80).setAutoExpand(true);
		}
		
		//追加缓存区数据
		public void append(IoBuffer in) {
			this.getBuf().put(in);
		}
		
		//重置
		public void rest() {
			decoder.reset();
		}
		
		public IoBuffer getBuf() {
			return buf;
		}
		public void setBuf(IoBuffer buf) {
			this.buf = buf;
		}
		public CharsetDecoder getDecoder() {
			return decoder;
		}
		
		
	}
}	
