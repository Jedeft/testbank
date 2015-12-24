package com.ncu.testbank.admin.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.data.Clazz;
import com.ncu.testbank.base.response.PageInfo;

public interface IClazzService {
	/**
	 * 检索clazz信息
	 * 
	 * @param page
	 * @param clazz
	 * @return
	 */
	public List<Clazz> searchData(PageInfo page, Clazz clazz);

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
	 * 批量删除class信息
	 * 
	 * @param class_id
	 */
	public void deleteData(List<String> class_id);

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
	 * 录入csv入库
	 * 
	 * @param fileName
	 *            文件名
	 * @param path
	 *            文件路径
	 * @param file
	 *            multipartFile文件
	 */
	public void loadCsv(String fileName, String path, MultipartFile file);
}
