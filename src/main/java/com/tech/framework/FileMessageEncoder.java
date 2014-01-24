package com.tech.framework;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

public class FileMessageEncoder extends MessageToByteEncoder<FileMessage>{

	@Override
	protected void encode(ChannelHandlerContext ctx, FileMessage msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("encode ..... ");
		byte cmd = msg.getCmd();
		byte file_count = msg.getFile_count();
		List<Byte> file_suffix_list = msg.getFile_suffix_list();
		List<byte[]> file_content_list = msg.getFile_content_list();
		byte file_suffix = file_suffix_list.get(0);
		byte[] file_content = file_content_list.get(0);
		int byte_len = 1 + 1 + file_suffix_list.size() + 4 + file_content.length;
		byte[] file_len = CodeUtil.int2byte_arr(file_content.length);
		byte[] content_byte = new byte[byte_len];
		content_byte[0] = cmd;
		content_byte[1] = file_count;
		content_byte[2] = file_suffix;
		System.arraycopy(content_byte, 0, content_byte, 3, 4);
		System.arraycopy(content_byte, 0, file_len, 7, file_content.length);
		System.out.println("message length ... " + content_byte.length);
		out.writeBytes(content_byte);
	}

}
