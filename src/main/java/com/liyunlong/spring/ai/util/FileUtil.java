package com.liyunlong.spring.ai.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil {
	
	public static List<String> readFile(String filepath){
		List<String> list = new ArrayList<String>();
		try {
			File file = new File(filepath);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String s;
			while((s = reader.readLine()) != null){
				list.add(s);
			}
			reader.close();
		} catch (Exception e) {
			log.error("readFile filepath["+filepath+"] error.", e);
		}
		return list;
	}
	
	public static String readFileContent(String filepath){
		BufferedReader reader = null;
		try {
			StringBuffer sb = new StringBuffer();
			File file = new File(filepath);
			reader = new BufferedReader(new FileReader(file));
			String s;
			while((s = reader.readLine()) != null){
				sb.append(s).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			log.error("readFile filepath["+filepath+"] error.", e);
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					log.error("readFile release resource filepath["+filepath+"] error.", e);
				}
			}
		}
		
		return null;
	}
	
	public static void writeFile(String filepath, String content) {
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(filepath);
			fw.write(content);
			fw.flush();
		} catch (Exception e) {
			log.error("writeFile filepath["+filepath+"] error.", e);
		} finally {
			if(fw != null) {
				try {
					fw.close();
				} catch (Exception e) {
					log.error("writeFile filepath["+filepath+"] release resource error.", e);
				}
			}
		}
	}
}
