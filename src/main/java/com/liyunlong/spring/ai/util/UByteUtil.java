package com.liyunlong.spring.ai.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.liyunlong.spring.ai.constant.UByteConstant;
import com.liyunlong.spring.ai.model.UByteData;
import com.liyunlong.spring.ai.model.UbyteFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UByteUtil {

	public static void main(String[] args) {
//		String filepath1 = "D:/train-labels.idx1-ubyte";
//		String content1 = readUByteFile(filepath1);
//		FileUtil.writeFile("D:/idx1.txt", content1);
		
//		String filepath3 = "D:/train-images.idx3-ubyte";
//		String content3 = readUByteFile(filepath3);
//		FileUtil.writeFile("D:/idx3.txt", content3);
		
		readJsonFromFile();
	}
	
	public static String readUByteFile(String filepath) {
		InputStream in = null;
		
		try {
			List<Integer> content = new ArrayList<Integer>();
			long t1 = System.currentTimeMillis();
			in = new FileInputStream(filepath);
			String dataType = null;
			int dimension = 1;
			int totalNum = 0;
			int rowNum = 0;
			int colNum = 0;
			
			int value;
			in.read();
			in.read();
			int index = 3;
			while((value = in.read()) != -1) {
				if(index < 9) {//先算基础信息
					if(index == 3) {
						dataType = convert2Hex(value);
					}else if(index == 4) {//判断数据维度
						dimension = value;
					}else {//5、6、7、8组合起来得到总数
						totalNum = totalNum * 256 + value;
					}
					index++;
				}else {
					if(dimension == 1) {//一维数据，从第7开始就是真实数据
						content.add(value);
						while((value = in.read()) != -1) {
							content.add(value);
						}
						break;
					}else {//还要先算行数和列数
						if(index < 17) {
							if(index < 13) {//9、10、11、12组合起来得到行数
								rowNum = rowNum * 256 + value;
							}else {//13、14、15、16组合起来得到列数
								colNum = colNum * 256 + value;
							}
							index++;
						}else {
							content.add(value);
							while((value = in.read()) != -1) {
								content.add(value);
							}
							break;
						}
					}
				}
			}
			UbyteFile ubFile = new UbyteFile();
			ubFile.setDataType(dataType);
			ubFile.setDimension(dimension);
			ubFile.setTotalNum(totalNum);
			ubFile.setRowNum(rowNum);
			ubFile.setColNum(colNum);
			ubFile.setContent(content);
			long t2 = System.currentTimeMillis();
			
			log.info("readUByteFile filepath["+filepath+"] read and parse file costTime:" + (t2 - t1));
			String str = JsonUtil.parseObject(ubFile);
			log.info("readUByteFile filepath["+filepath+"] parse object to json costTime:" + (System.currentTimeMillis() - t2));
			
			return str;
		} catch (Exception e) {
			log.error("readUByteFile filepath["+filepath+"] error.", e);
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (Exception e) {
					log.error("readUByteFile filepath["+filepath+"] release resource error.", e);
				}
			}
		}
		
		return null;
	}
	
	public static String convert2Hex(int value) {
		if(value < 10) {
			return "0" + value;
		}else if(value < 16) {
			return "0" + (char)((value - 10) + UByteConstant.UBYTE_CAPITAL);
		}
		StringBuffer sb = new StringBuffer();
		
		int v1 = value % 16;
		int v2 = (value / 16) % 16;
		if(v2 < 10) {
			sb.append(v2);
		}else {
			sb.append((char)((v2 - 10) + UByteConstant.UBYTE_CAPITAL));
		}
		if(v1 < 10) {
			sb.append(v1);
		}else {
			sb.append((char)((v1 - 10) + UByteConstant.UBYTE_CAPITAL));
		}
		
		return sb.toString();
	}
	
	public static void readJsonFromFile() {
		long t1 = System.currentTimeMillis();
		String file1 = "D:/idx1.txt";
		String file3 = "D:/idx3.txt";
		String json1 = FileUtil.readFileContent(file1);
		String json3 = FileUtil.readFileContent(file3);
		UbyteFile ub1 = JsonUtil.convert(json1, UbyteFile.class);
		UbyteFile ub3 = JsonUtil.convert(json3, UbyteFile.class);
		long t2 = System.currentTimeMillis();
		log.info("readJsonFromFile convert json to object costTime:" + (t2 - t1));
		
		int rowNum = ub3.getRowNum();
		int colNum = ub3.getColNum();
		
		List<Integer> values1 = ub1.getContent();
		List<Integer> values3 = ub3.getContent();
		List<UByteData> datas = new ArrayList<UByteData>();
		
		int batchNum = 6000;
		int index = 0;
		for (int k = 0;k < values1.size();) {
			UByteData ubData = new UByteData();
			
			int[][] matrix = new int[rowNum][colNum];
			for (int i = 0; i < rowNum; i++) {
				for (int j = 0; j < colNum; j++) {
					matrix[i][j] = values3.get(index++);
				}
			}
			ubData.setMatrix(matrix);
			ubData.setValue(values1.get(k));
			
			System.out.println(JsonUtil.parseObject(ubData));
			if(k == 0) {
				break;
			}
			datas.add(ubData);
			k++;
			if(k % batchNum == 0) {
				FileUtil.writeFile("D:/study/data" + (k / batchNum) + ".txt", JsonUtil.parseObject(datas));
				datas.clear();
			}
		}
		log.info("readJsonFromFile write to file costTime:" + (System.currentTimeMillis() - t2));
	}
}
