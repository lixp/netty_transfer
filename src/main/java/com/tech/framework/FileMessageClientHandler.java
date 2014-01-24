package com.tech.framework;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.example.factorial.FactorialClientHandler;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;



public class FileMessageClientHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = Logger.getLogger(FactorialClientHandler.class.getName());

    private ChannelHandlerContext ctx;
    private final ByteBuf byteBuf;
    
    public FileMessageClientHandler(final byte[] byteMessage) {
        byteBuf = Unpooled.wrappedBuffer(byteMessage);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg; // (1)
        try {
            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        } finally {
            m.release();
        }
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.", cause);
        ctx.close();
    }
    
}
