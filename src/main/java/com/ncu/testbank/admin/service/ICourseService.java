package com.ncu.testbank.admin.service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.data.Course;
import com.ncu.testbank.base.response.PageInfo;

public interface ICourseService {
	/**
	 * 检索course信息
	 * @param page
	 * @param course
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	public List<Course> searchData(PageInfo page, Course course)
			throws IllegalAccessException, InstantiationException,
			InvocationTargetException, IntrospectionException;

	/**
	 * 插入一条course信息
	 * 
	 * @param course
	 */
	public void insertOne(Course course);

	/**
	 * 删除一条course信息
	 * 
	 * @param course_id
	 */
	public void deleteOne(String course_id);

	/**
	 * 更新一条course信息
	 * 
	 * @param course
	 */
	public void updateOne(Course course);

	/**
	 * 获取一条course信息
	 * 
	 * @param course_id
	 */
	public Course getCourse(String course_id);

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
