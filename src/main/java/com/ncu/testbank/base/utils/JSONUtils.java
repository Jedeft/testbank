package com.ncu.testbank.base.utils;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.alibaba.fastjson.JSON;

/**
 * json工具类
 * @author Rong.zhu
 *
 */
public class JSONUtils{
	private static ObjectMapper mapper = new ObjectMapper();
	private static Logger log = Logger.getLogger(JSONUtils.class);
	
	/**
	 * 将json数据转成指定类型的对象
	 * @param content
	 * @param valueType
	 * @return
	 */

	public static <T>T convertJson2Object(String content, Class<T> valueType){
		if(content == null || content.equals("")) return null;
		 
		return JSON.parseObject(content, valueType);
	}
	
	public static <T>T convertDataJson2Object(String content, Class<T> valueType){
		if(content == null || content.equals("")) return null;
		//if(content)
		return JSON.parseObject(content, valueType);
	}
	
	/**
	 * 将对象格式化成json格式数据
	 * @param obj
	 * @return
	 */
	public static String convertObject2Json(Object obj) {
		return JSON.toJSONString(obj, true);
	}
	
	/**
	 * 将json格式数据转成Map对象
	 * @param content
	 * @return
	 */
	public static Map<String, Object> convertJson2Map(String content){
		try {
			return mapper.readValue(content,  new TypeReference<Map<String,Object>>() { });
		} catch (IOException e) {
			log.error("json解析错误:"+e.getMessage());
		}
		return null;
	}
}

