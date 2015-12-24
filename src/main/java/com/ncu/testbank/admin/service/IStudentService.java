package com.ncu.testbank.admin.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.data.Student;
import com.ncu.testbank.base.response.PageInfo;

public interface IStudentService {
	/**
	 * 检索student信息
	 * 
	 * @param page
	 * @param student
	 * @return
	 */
	public List<Student> searchData(PageInfo page, Student student);

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
	 * 批量删除student信息
	 * 
	 * @param student_id
	 */
	public void deleteData(List<String> student_id);

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
