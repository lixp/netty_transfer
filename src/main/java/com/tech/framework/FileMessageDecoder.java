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
	private int id_capacity;//id count
	private int file_content_capacity;//file count 
	private int file_suffix_capacity;//file suffix count
	private int file_len;
	private List<String> id_str = new ArrayList<String>();// multiple file id that composite photo
	private List<Byte> suffix_byte = new ArrayList<Byte>();// multiple file to upload to fastdfs, each file's suffix is required
	private List<byte[]> file_content_list = new ArrayList<byte[]>(); // collect each file's content formed in byte array
	private Map<String,Object> outMap = new HashMap<String,Object>(); // the result to the next pipeline
	public FileMessageDecoder() {
		// Set the initial state.
		super(FileMessageState.CMD);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		switch (state()) {
		//read the first byte to decide which cmd to exec
	      case CMD:
	       cmd = buf.readByte();
	       outMap.put("cmd",cmd);
	       if(cmd < 3) {
	    	   checkpoint(FileMessageState.ID_SIZE);
	       }
	       if(cmd >= 3) {
	    	   checkpoint(FileMessageState.FILE_COUNT);
	       }
	       break;
	     //if the cmd is delete or composite, the picture count
	     case ID_SIZE:
	       id_size = buf.readByte();
	       id_capacity = id_size;
	       outMap.put(FileInfoConstant.FILE_ID_COUNT, id_size);
	       checkpoint(FileMessageState.ID_LENGTH);
	       break;
	     //each file id content byte array length
	     case ID_LENGTH:
	       id_len = buf.readInt();
	       checkpoint(FileMessageState.ID_VALUE);
	       break;
	     // each file id content  byte array
	     case ID_VALUE:
	       ByteBuf buf_tmp = buf.readBytes(id_len);//read the content
	       id_size--;//if the id has been read finished ,the position is important, can not be placed in head
	       String id_str_tmp = new String(buf_tmp.array());//convert to string
	       if(id_capacity == 1){
	    	   //if the operator is only one
    		   outMap.put(FileInfoConstant.FILE_ID, id_str_tmp); // put the content into result
    		   out.add(outMap);
    		   return;
    	   }
	       if(id_capacity > 1) {
	    	   //if the operator is more than one
	    	   id_str.add(id_str_tmp);
	       }
	       if(id_size < 1) {
	    	   //if the operator is the last one
	    	   outMap.put(FileInfoConstant.FILE_ID_LIST,id_str);
		       out.add(outMap);
		       return;
	       }
	       if(id_size >= 1) {
	    	   //if the operator is not the last one, turn back to ID_LENGTH
	    	   checkpoint(FileMessageState.ID_LENGTH); 
	       }
	       break;
	     //if the cmd is multiple file upload
	     case FILE_COUNT:
	       //read the first byte that record the file count
	       file_count_content = buf.readByte();
	       file_count_suffix = file_count_content;
	       file_content_capacity = file_count_content;
	       file_suffix_capacity = file_count_content;
	       outMap.put(FileInfoConstant.FILE_CONTENT_COUNT, file_suffix_capacity);
	       checkpoint(FileMessageState.FILE_SUFFIX); 
	       break;
	     //read the suffix and then match the file suffix
	     case FILE_SUFFIX:
	       //count the file processed
	       Byte suffix_byte_tmp = buf.readByte();//the byte is the flag to indicate the file suffix
	       file_count_suffix--;//the position is very important ,can not be placed in head line 1
	       if(file_suffix_capacity == 1) {//if the file is the only one, put the singal object into result
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
	    	   break;
	       }
	     // read the file content length 
	     case FILE_LENGTH:
	    	file_len = buf.readInt();
	    	checkpoint(FileMessageState.FILE_VALUE);
	     case FILE_VALUE:
	    	//count the file read
	    	ByteBuf file_arr_tmp = buf.readBytes(file_len);
	    	file_count_content--;//cause net io loop read ,just subtract 1 once, if place in head, the value will be negative
	    	//read the file content
	    	byte[] file_content_arr_tmp = file_arr_tmp.array();
	    	if(file_content_capacity == 1) {//if the file count is 1
	    		outMap.put(FileInfoConstant.FILE_CONTENT, file_content_arr_tmp);
	    		out.add(outMap);
	    		return;
	    	}
	    	file_content_list.add(file_content_arr_tmp);
	    	if(file_count_content < 1) {//indicate that the file is the last one and then return the result
	    		outMap.put(FileInfoConstant.FILE_CONTENT_LIST, file_content_list);
	    		out.add(outMap);
	    		return;
	    	}
	    	if(file_count_content >= 1) {
	    		checkpoint(FileMessageState.FILE_LENGTH);
	    		break;
	    	}
	      default:
	       throw new Error("Shouldn't reach here.");
	    }
	}
	public void reset() {
		
	}
	
}
/**
 * CMD 存储命令：单个字节存储
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