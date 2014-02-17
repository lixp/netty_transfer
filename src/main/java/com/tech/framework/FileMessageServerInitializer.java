package com.tech.framework;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class FileMessageServerInitializer extends ChannelInitializer<SocketChannel> {
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline = ch.pipeline();
		EventExecutorGroup e1 = new DefaultEventExecutorGroup(16);
		EventExecutorGroup e2 = new DefaultEventExecutorGroup(8);
		pipeline.addLast(e1, new FileMessageDecoder());
        pipeline.addLast(e2, new FileMessageServerHandler());
	}

}
