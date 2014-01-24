package com.tech.framework;

/**
 * 状态机的流转，最后输出的list顺序强烈一致
 * 0.命令
 * 1.id大小
 * 2.1+id的值
 * 3.文件的数量
 * 4.1+文件的后缀
 * 5.1+文件的字节流
 * @author lixp
 *
 */
public enum FileMessageState {
    CMD,
    ID_SIZE,
    ID_LENGTH,
    ID_VALUE,
    FILE_COUNT,
    FILE_SUFFIX,
    FILE_LENGTH,
    FILE_VALUE
}
