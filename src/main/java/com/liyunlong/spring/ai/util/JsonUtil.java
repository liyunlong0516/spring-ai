package com.liyunlong.spring.ai.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static String parseObject(Object object) {
		if(object == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			log.error("parseObject error.", e);
		}
		return null;
	}
	
	public static <T> T convert(String json, Class<T> clazz) {
		if(json == null || json.trim().length() == 0) {
			return null;
		}
		try {
			return objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			log.error("convert error.", e);
		}
		
		return null;
	}
	
	public static <T> T convert(String json, TypeReference<T> reference) {
		if(json == null || json.trim().length() == 0) {
			return null;
		}
		try {
			return objectMapper.readValue(json, reference);
		} catch (Exception e) {
			log.error("convert error.", e);
		}
		
		return null;
	}
}
