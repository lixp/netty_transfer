package com.tech.framework;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.stream.FileImageOutputStream;

public class FileMessageServerHandler  extends ChannelInboundHandlerAdapter{
		@Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) {
			Map m = (HashMap) msg; // (1)
	        //发送命令
			Object cmdObj = m.get(FileInfoConstant.CMD);
	        int cmd = 0;
	        if(cmdObj != null){
	        	cmd = (int)cmdObj;
	        }
	        
	        //单个文件字节流
	        Object file_content_obj = (Object)m.get(FileInfoConstant.FILE_CONTENT);
	        byte[] file_content = null;
	        if(file_content_obj != null) {
	        	file_content = (byte[])file_content_obj;
	        }
	        Object file_content_list_obj = m.get(FileInfoConstant.FILE_CONTENT_LIST);
	        List<byte[]> file_content_list = null;
	        //多个文件字节流
	        if(file_content_list_obj != null) {
	        	 file_content_list = (List<byte[]>) file_content_list_obj;
	        }
	        Object file_suffix_list_obj = m.get(FileInfoConstant.FILE_SUFFIX_LIST);
	        List<Byte> file_suffix_list = null;
	        if(file_suffix_list_obj != null){
	        	file_suffix_list = (List<Byte>)file_suffix_list_obj;
	        }
	        //单个文件后缀名
	        Object file_suffix_obj = m.get(FileInfoConstant.FILE_SUFFIX);
	        byte file_suffix = 0;
	        if(file_suffix_obj != null){
	        	file_suffix = (byte)file_suffix_obj;
	        }
	        //文件数目
	        Object file_count_obj = m.get(FileInfoConstant.FILE_CONTENT_COUNT);
	        int file_count = 0;
	        if(file_count_obj != null) {
	        	file_count = (int)file_count_obj;
	        }
	        //文件id数目
	        Object id_count_obj = m.get(FileInfoConstant.FILE_ID_COUNT);
	        int id_count = 0;
	        if(id_count_obj != null) {
	        	id_count = (int)id_count;
	        }
	        System.out.println("本次发送的命令为：" + CodeUtil.getField(cmd) + "文件数目：" + file_count + "文件Id数目：" + id_count);
	    }

	    @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	        cause.printStackTrace();
	        ctx.close();
	    }
	    public String toMapString(Map msg){
	    	Map m = (HashMap) msg; // (1)
	        //发送命令
	        int cmd = (int)m.get(FileInfoConstant.CMD);
	        //单个文件字节流
	        byte[] file_content = (byte[])m.get(FileInfoConstant.FILE_CONTENT);
	        //多个文件字节流
	        List<byte[]> file_content_list = (List<byte[]>) m.get(FileInfoConstant.FILE_CONTENT_LIST);
	        //单个文件后缀名
	        byte file_suffix = (byte)m.get(FileInfoConstant.FILE_SUFFIX);
	        //多个文件后缀名
	        List<Byte> file_suffix_list = (List<Byte>)m.get(FileInfoConstant.FILE_SUFFIX_LIST);
	        //文件数目
	        byte file_count = (byte)m.get(FileInfoConstant.FILE_CONTENT_COUNT);
	        //文件id数目
	        byte id_count = (byte)m.get(FileInfoConstant.FILE_ID_COUNT);
	    	return "本次发送的命令为：" + CodeUtil.getField(cmd) + "文件数目：" + file_count + "文件Id数目：" + id_count;
	    }
}
