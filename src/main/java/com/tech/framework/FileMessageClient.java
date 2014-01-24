package com.tech.framework;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class FileMessageClient {
	public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = Integer.parseInt("8080");
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        File file1 = new File("D:\\Chrysanthemum.jpg");
        File file = new File("D:\\Desert.jpg");
    	FileMessage fileMessage = new FileMessage();
    	byte cmd = 3;
    	Byte suffix = 1;
    	Byte suffix_1 = 2;
    	byte file_count = 2;
 		byte[] fileBytes = IOUtils.toByteArray(new FileInputStream(file));
 		byte[] fileBytes1 = IOUtils.toByteArray(new FileInputStream(file1));
 		List<byte[]> file_bytes = new ArrayList<byte[]>();
 		file_bytes.add(fileBytes);
 		file_bytes.add(fileBytes1);
 		List<Byte> file_suffix_list = new ArrayList<Byte>();
 		file_suffix_list.add(suffix);
 		file_suffix_list.add(suffix_1);
 		fileMessage.setFile_count(file_count);
 		fileMessage.setCmd(cmd);
 		fileMessage.setFile_content_list(file_bytes);
 		fileMessage.setFile_suffix_list(file_suffix_list);
 		System.out.println(file.length());
 		System.out.println(fileBytes.length);
 		System.out.println("..........end");
		byte file_suffix = file_suffix_list.get(0);
		byte[] file_content = file_bytes.get(0);
		byte[] file_content1 = file_bytes.get(1);
		int byte_len = 1 + 1 + file_suffix_list.size() + 4 + file_content.length + 4 + file_content1.length;
		byte[] file_len = CodeUtil.int2byte_arr(file_content.length);
		byte[] file1_len = CodeUtil.int2byte_arr(file_content1.length);
		byte[] content_byte = new byte[byte_len];
		content_byte[0] = cmd;
		content_byte[1] = file_count;
		content_byte[2] = file_suffix;
		System.arraycopy(file_len, 0, content_byte, 3, 4);
		System.arraycopy(file_content, 0, content_byte, 7, file_content.length);
		System.arraycopy(file1_len, 0, content_byte, 7 + file_content.length, 4);
		System.arraycopy(file_content1, 0, content_byte, 7 + file_content.length + 4, file_content1.length);
		System.out.println("message length ... " + content_byte.length);
		//out.writeBytes(content_byte);
        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new FileMessageClientInitializer(content_byte));
            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
