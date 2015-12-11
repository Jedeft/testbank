package com.ncu.testbank.admin.service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.data.Academy;
import com.ncu.testbank.base.response.PageInfo;

public interface IAcademyService {

	/**
	 * 检索academy信息
	 * @param page
	 * @param academy
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	public List<Academy> searchData(PageInfo page, Academy academy)
			throws IllegalAccessException, InvocationTargetException,
			IntrospectionException;

	/**
	 * 插入一条academy信息
	 * 
	 * @param academy
	 */
	public void insertOne(Academy academy);

	/**
	 * 删除一条academy信息
	 * 
	 * @param academy_id
	 */
	public void deleteOne(String academy_id);

	/**
	 * 更新一条academy信息
	 * 
	 * @param academy
	 */
	public void updateOne(Academy academy);
	
	/**
	 * 批量删除academy信息
	 * 
	 * @param academy_id
	 */
	public void deleteData(List<String> academy_id);

	/**
	 * 获取一条academy信息
	 * 
	 * @param academy_id
	 */
	public Academy getAcademy(String academy_id);

	/**
	 * 录入csv入库
	 * @param fileName 文件名
	 * @param path 文件路径
	 * @param file multipartFile文件
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void loadCsv(String fileName, String path, MultipartFile file)
			throws IllegalStateException, IOException;
}
