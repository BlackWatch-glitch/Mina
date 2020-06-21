package com.protocal;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/*
 * 编码器
 */
public class ProtocalEncoder extends ProtocolEncoderAdapter {

	//定义数据包的编码形式
	private final Charset charset;
	//构造函数进行传输
	public ProtocalEncoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		ProtocalPack value = (ProtocalPack) message;//报文
		IoBuffer buf = IoBuffer.allocate(value.getLength());	//设置缓存区大小，即报文的长度
		buf.setAutoExpand(true);			//设置缓冲区自动增长
		buf.putInt(value.getLength());		//设置包头
		buf.put(value.getFlag());			//方字节
		if (value.getContent() != null) {		//放置内容前进行判断
			buf.put(value.getContent().getBytes());		//
		}
		buf.flip();		
		out.write(buf);
	}

}
