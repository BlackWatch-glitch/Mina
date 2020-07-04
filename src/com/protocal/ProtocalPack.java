package com.protocal;

/*
 * 自定义协议包的开发
 */
public class ProtocalPack {
	private int length;
	private byte flag;
	private String content;
	
	public ProtocalPack(byte flag, String contetn) {
		this.flag = flag;
		this.content = content;	//内容主体
		int len1 = content == null ? 0 : content.getBytes().length;
		this.length = 5 + len1;		//5=4+1,int4个字节，byte1哥字节
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("length:").append(length);
		sb.append("flag:").append(flag);
		sb.append("content:").append(content);
		return sb.toString();
	}
}
