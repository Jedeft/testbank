package com.ncu.testbank.admin.service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.data.Clazz;
import com.ncu.testbank.base.response.PageInfo;

public interface IClazzService {
	/**
	 * 检索class信息
	 * 
	 * @param clazz
	 * @return
	 */
	public List<Clazz> searchData(PageInfo page, Clazz clazz)
			throws IllegalAccessException, InstantiationException,
			InvocationTargetException, IntrospectionException;

	/**
	 * 插入一条class信息
	 * 
	 * @param clazz
	 */
	public void insertOne(Clazz clazz);

	/**
	 * 删除一条class信息
	 * 
	 * @param class_id
	 */
	public void deleteOne(String class_id);

	/**
	 * 更新一条class信息
	 * 
	 * @param clazz
	 */
	public void updateOne(Clazz clazz);

	/**
	 * 获取一条class信息
	 * 
	 * @param class_id
	 */
	public Clazz getClazz(String class_id);

	/**
	 * 录入csv文件数据到库中
	 * 
	 * @param in
	 */
	public void loadCsv(String fileName, String path, MultipartFile file)
			throws IllegalStateException, IOException;
}
