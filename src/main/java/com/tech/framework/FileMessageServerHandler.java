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
		@SuppressWarnings("unchecked")
		@Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) {
			Map<Object,Object> m = (HashMap<Object, Object>) msg; // (1)
	        //retrieve the command
			Object cmdObj = m.get(FileInfoConstant.CMD);
			int file_count = (int)m.get(FileInfoConstant.FILE_CONTENT_COUNT);
	        int cmd = 0;
	        if(cmdObj != null){
	        	cmd = (int)cmdObj;
	        }
	        //get the file id count (for downloading or composite) or file count
	        if(cmd == 4 || cmd == 3) {
		        	 //get the single file information
		        	 Object file_content_obj = (Object)m.get(FileInfoConstant.FILE_CONTENT);
		 	         byte [] file_content = null;
		 	         if(file_content_obj != null) {
			        	file_content = (byte[])file_content_obj;
			         }
		 	        //get the single file suffix
		 	        Object file_suffix_obj = m.get(FileInfoConstant.FILE_SUFFIX);
		 	        byte file_suffix = 0;
		 	        if(file_suffix_obj != null){
		 	        	file_suffix = (byte)file_suffix_obj;
		 	        }
		 	        System.out.println(file_content.length + "---file_content.length" + "file_suffix \t value " + file_suffix);
	        }
	        if(cmd == 5 || cmd == 6) {
	        	    //get the file list 
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
			        for(int i = 0; i < file_count; i++) {
			        	byte[] file_arr = file_content_list.get(i);
			        	byte file_suffix = file_suffix_list.get(i);
			        	System.out.println("file_arr" + i +"\t" + file_arr.length + "file_suffix_value" + i + "\t" + file_suffix);
			        }
			        System.out.println(file_content_list.size() + "\t file_content_list.length" + file_suffix_list.size() + "\t file_suffix_list.length");
	        }
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
