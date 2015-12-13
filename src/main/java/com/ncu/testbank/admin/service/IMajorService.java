package com.ncu.testbank.admin.service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.data.Major;
import com.ncu.testbank.base.response.PageInfo;

public interface IMajorService {
	/**
	 * 检索major信息
	 * 
	 * @param page
	 * @param major
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	public List<Major> searchData(PageInfo page, Major major)
			throws IllegalAccessException, InstantiationException,
			InvocationTargetException, IntrospectionException;

	/**
	 * 插入一条major信息
	 * 
	 * @param major
	 */
	public void insertOne(Major major);

	/**
	 * 删除一条major信息
	 * 
	 * @param major_id
	 */
	public void deleteOne(String major_id);

	/**
	 * 批量删除major信息
	 * 
	 * @param major_id
	 */
	public void deleteData(List<String> major_id);

	/**
	 * 更新一条major信息
	 * 
	 * @param major
	 */
	public void updateOne(Major major);

	/**
	 * 获取一条major信息
	 * 
	 * @param major_id
	 */
	public Major getMajor(String major_id);

	/**
	 * 录入csv入库
	 * 
	 * @param fileName
	 *            文件名
	 * @param path
	 *            文件路径
	 * @param file
	 *            multipartFile文件
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void loadCsv(String fileName, String path, MultipartFile file)
			throws IllegalStateException, IOException;
}
