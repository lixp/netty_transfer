package com.tech.framework;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;

public class FileMessageClientInitializer extends ChannelInitializer<SocketChannel> {
	private byte[] fileMessage;
	public FileMessageClientInitializer(byte[]  fileMessage) {
		this.fileMessage = fileMessage;
	}
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("handler", new FileMessageClientHandler(fileMessage));
   }

}
