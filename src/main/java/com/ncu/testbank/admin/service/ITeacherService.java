package com.ncu.testbank.admin.service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.data.Teacher;
import com.ncu.testbank.base.response.PageInfo;

public interface ITeacherService {
	/**
	 * 检索teacher信息
	 * 
	 * @param teacher
	 * @return
	 */
	public List<Teacher> searchData(PageInfo page, Teacher teacher)
			throws IllegalAccessException, InstantiationException,
			InvocationTargetException, IntrospectionException;

	/**
	 * 插入一条teacher信息
	 * 
	 * @param teacher
	 */
	public void insertOne(Teacher teacher);

	/**
	 * 删除一条teacher信息
	 * 
	 * @param teacher_id
	 */
	public void deleteOne(String teacher_id);

	/**
	 * 批量删除teacher信息
	 * 
	 * @param teacher_id
	 */
	public void deleteData(List<String> teacher_id);
	
	/**
	 * 更新一条teacher信息
	 * 
	 * @param teacher
	 */
	public void updateOne(Teacher teacher);

	/**
	 * 获取一条teacher信息
	 * 
	 * @param teacher_id
	 */
	public Teacher getTeacher(String teacher_id);

	/**
	 * 录入csv文件数据到库中
	 * 
	 * @param in
	 */
	public void loadCsv(String fileName, String path, MultipartFile file)
			throws IllegalStateException, IOException;
}
