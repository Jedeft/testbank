package com.ncu.testbank.admin.service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.data.Student;
import com.ncu.testbank.base.response.PageInfo;

public interface IStudentService {
	/**
	 * 检索student信息
	 * @param page
	 * @param student
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	public List<Student> searchData(PageInfo page, Student student)
			throws IllegalAccessException, InstantiationException,
			InvocationTargetException, IntrospectionException;

	/**
	 * 插入一条student信息
	 * 
	 * @param student
	 */
	public void insertOne(Student student);

	/**
	 * 删除一条student信息
	 * 
	 * @param student_id
	 */
	public void deleteOne(String student_id);

	/**
	 * 更新一条student信息
	 * 
	 * @param student
	 */
	public void updateOne(Student student);

	/**
	 * 获取一条student信息
	 * 
	 * @param student_id
	 */
	public Student getStudent(String student_id);

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
