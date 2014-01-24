package com.tech.framework;

import java.util.List;

/**
 * 
 * @author lixp
 *
 */
public class FileMessage {
	/**
	 * 文件操作命令  单个字节
	 */
    private byte cmd;
    /**
     * id文件数量 单个字节
     */
    private byte id_size;
    /**
     *id内容
     */
    private List<String> id_list;
    /**
     * 文件数量 单个字节
     */
    private byte file_count;
    /**
     * 文件后缀名列表 单个字节映射
     */
    private List<Byte> file_suffix_list;
    /**
     * 文件内容列表
     */
	private List<byte[]> file_content_list;
	public byte getCmd() {
		return cmd;
	}
	public void setCmd(byte cmd) {
		this.cmd = cmd;
	}
	public byte getId_size() {
		return id_size;
	}
	public void setId_size(byte id_size) {
		this.id_size = id_size;
	}
	public List<String> getId_list() {
		return id_list;
	}
	public void setId_list(List<String> id_list) {
		this.id_list = id_list;
	}
	public byte getFile_count() {
		return file_count;
	}
	public void setFile_count(byte file_count) {
		this.file_count = file_count;
	}
	public List<Byte> getFile_suffix_list() {
		return file_suffix_list;
	}
	public void setFile_suffix_list(List<Byte> file_suffix_list) {
		this.file_suffix_list = file_suffix_list;
	}
	public List<byte[]> getFile_content_list() {
		return file_content_list;
	}
	public void setFile_content_list(List<byte[]> file_content_list) {
		this.file_content_list = file_content_list;
	}
	
    
}
