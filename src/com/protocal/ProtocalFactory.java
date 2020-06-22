package com.protocal;

import java.nio.charset.Charset;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


/*
 * 设置编解码的工厂,用于获得编解码器的工厂
 */
public class ProtocalFactory implements ProtocolCodecFactory{

	private final ProtocalDecoder decoder;
	private final ProtocalEncoder encoder;
	
	public ProtocalFactory(Charset charset) {
		encoder = new ProtocalEncoder(charset);
		decoder = new ProtocalDecoder(charset);
	}
	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		
		return decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		
		return encoder;
	}
	
	
}
