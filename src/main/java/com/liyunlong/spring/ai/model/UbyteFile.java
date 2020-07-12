package com.liyunlong.spring.ai.model;

import java.util.List;

import lombok.Data;

@Data
public class UbyteFile {

	/**
	 * 0x08：无符号数字（byte），0x09：有符号数字（byte），0x0B：short（2 bytes）
	 * 0x0C：int（4 bytes），0x0D：float（4 bytes），0x0E：double（8 bytes）
	 */
	private String dataType;
	
	/**
	 * 数据维度，1：一维，2：二维，3：三维……依次类推。
	 */
	private int dimension;
	
	/**
	 * 数据总数
	 */
	private long totalNum;
	
	/**
	 * 每一个数据的行数，dimension=3时有意义
	 */
	private int rowNum;
	
	/**
	 * 每一个数据的列数，dimension=3时有意义
	 */
	private int colNum;
	
	/**
	 * 先把数据按顺序存储，后续再二次解析
	 */
	private List<Integer> content;
	
}
