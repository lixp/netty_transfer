Introduction:

1.this small , simple project is playing a role as server based in netty(nio)

2.this is just getting started, there are some of that things that i will do with netty_transfer
	
	a) multiple files transfer or push message that representation form is byte-streamed
	b) multiple file identifies transfer for downloading

3.packet form:<br/>
		
	+-----------------+------------------------+--------------------------+-----------------------------+ 
	|				  |                        |						  |							    | 
	+----CMD(1byte)---+---PARAM_COUNT(1byte)---+----PARAM_LENGTH(4byte)---+---PARAM_CONTENT(*byte(s))---+ 
	|	  			  |						        													| 
	|    OPERATION    | PER PARAM  CONSISTS OF THREE PARTS ABOVE(e.g:file name ,file length,file owner) | 
	|				  |																				    | 
	+-----------------+---------------------------------------------------------------------------------+	 	

4.distribute file system is fastdfs,  fastdfs's author is YUQING(余庆)，employed by Alibaba Corporation.
    