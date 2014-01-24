package com.tech.framework;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 文件字节解码
 * @author lixp
 *
 */
public class FileMessageDecoder extends ReplayingDecoder<FileMessageState> {
	private int cmd;
	private int id_size;
	private int id_len;
	private int file_count_suffix;
	private int file_count_content;
	private int id_capacity;//id的容量
	private int file_content_capacity;//文件内容的容量
	private int file_suffix_capacity;//文件后缀名的容量
	private int file_len;
	private List<String> id_str = new ArrayList<String>();
	private List<Byte> suffix_byte = new ArrayList<Byte>();
	private List<byte[]> file_content_list = new ArrayList<byte[]>();
	private Map<String,Object> outMap = new HashMap<String,Object>();
	public FileMessageDecoder() {
		// Set the initial state.
		super(FileMessageState.CMD);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("decode.......");
		switch (state()) {
	      case CMD:
	       cmd = buf.readByte();
	       System.out.println(cmd+".......cmd");
	       outMap.put("cmd",cmd);
	       if(cmd < 3) {
	    	   checkpoint(FileMessageState.ID_SIZE);
	       }
	       if(cmd >= 3) {
	    	   checkpoint(FileMessageState.FILE_COUNT);
	       }
	       break;
	     case ID_SIZE:
	       id_size = buf.readByte();
	       id_capacity = id_size;
	       outMap.put(FileInfoConstant.FILE_ID_COUNT, id_size);
	       checkpoint(FileMessageState.ID_LENGTH);
	       break;
	     case ID_LENGTH:
	       id_len = buf.readInt();
	       checkpoint(FileMessageState.ID_VALUE);
	       break;
	     case ID_VALUE:
	       //图片情况
	       id_size--;
	       ByteBuf buf_tmp = buf.readBytes(id_len);
	       String id_str_tmp = new String(buf_tmp.array());
	       if(id_capacity == 1){
	    	   //处理单个file_id的操作
    		   outMap.put(FileInfoConstant.FILE_ID, id_str_tmp);
    		   out.add(outMap);
    		   return;
    	   }
	       if(id_capacity > 1) {
	    	   //处理多个file_id的操作
	    	   id_str.add(id_str_tmp);
	       }
	       outMap.put(FileInfoConstant.FILE_ID_LIST,id_str);
	       if(id_size < 1) {
	    	   out.add(outMap);
		       return;
	       }
	       if(id_size >= 1) {
	    	   checkpoint(FileMessageState.ID_LENGTH); 
	       }
	       break;
	     case FILE_COUNT:
	       file_count_content = buf.readByte();
	       file_count_suffix = file_count_content;
	       file_content_capacity = file_count_content;
	       file_suffix_capacity = file_count_content;
	       outMap.put(FileInfoConstant.FILE_CONTENT_COUNT, file_suffix_capacity);
	       checkpoint(FileMessageState.FILE_SUFFIX); 
	       break;
	     case FILE_SUFFIX:
	       file_count_suffix--;
	       Byte suffix_byte_tmp = buf.readByte();
	       if(file_suffix_capacity == 1) {
	    	   outMap.put(FileInfoConstant.FILE_SUFFIX, suffix_byte_tmp);
	    	   checkpoint(FileMessageState.FILE_LENGTH);
	    	   break;
	       }
	       suffix_byte.add(suffix_byte_tmp);
	       if(file_count_suffix < 1) {
	    	   outMap.put(FileInfoConstant.FILE_SUFFIX_LIST, suffix_byte);
	    	   checkpoint(FileMessageState.FILE_LENGTH);
	       }
	       if(file_count_suffix >= 1) {
	    	   checkpoint(FileMessageState.FILE_SUFFIX);
	       }
	       break;
	     case FILE_LENGTH:
	    	file_len = buf.readInt();
	    	checkpoint(FileMessageState.FILE_VALUE);
	    	break;
	     case FILE_VALUE:
	    	file_count_content--;
	    	ByteBuf file_arr_tmp = buf.readBytes(file_len);
	    	byte[] file_content_arr_tmp = file_arr_tmp.array();
	    	if(file_content_capacity == 1) {
	    		outMap.put(FileInfoConstant.FILE_CONTENT, file_content_arr_tmp);
	    		out.add(outMap);
	    		return;
	    	}
	    	file_content_list.add(file_content_arr_tmp);
	    	if(file_count_content < 1) {
	    		outMap.put(FileInfoConstant.FILE_CONTENT_LIST, file_content_list);
	    		out.add(outMap);
	    		return;
	    	}
	    	if(file_count_content >= 1) {
	    		checkpoint(FileMessageState.FILE_LENGTH);
	    	}
	    	break;
	      default:
	       throw new Error("Shouldn't reach here.");
	    }
	}
	public void reset() {
		
	}
	
}
/**
 * CMD 存储命令：
 * 2 单个字节存储
 * 3 上传单张图片不压缩
 * 4 上传单张图片压缩
 * 5 上传多张图片不压缩
 * 6 上传多张图片压缩
 * 7 合成多张图片
 * 1 删除单张图片
 * 2 删除多张图片
**/
/**
* suffix 后缀名称：单个字节存储
* 1 jpg
* 2 png
* 3 jpeg
* 4 gif
* 5 bmp
* 6 amr
* 7 zip
**/