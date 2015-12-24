package com.ncu.testbank.admin.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.data.Syllabus;
import com.ncu.testbank.admin.data.view.SyllabusView;
import com.ncu.testbank.base.response.PageInfo;

public interface ISyllabusService {

	/**
	 * 检索syllabus信息
	 * 
	 * @param page
	 * @param syllabus
	 * @return
	 */
	public List<SyllabusView> searchData(PageInfo page, Syllabus syllabus);

	/**
	 * 插入一条syllabus信息
	 * 
	 * @param syllabus
	 */
	public void insertOne(Syllabus syllabus);

	/**
	 * 删除一条syllabus信息
	 * 
	 * @param syllabus
	 */
	public void deleteOne(String syllabus_id);

	/**
	 * 批量删除syllabus信息
	 * 
	 * @param syllabus_id
	 */
	public void deleteData(List<String> syllabus_id);

	/**
	 * 更新一条syllabus信息
	 * 
	 * @param syllabus
	 */
	public void updateOne(Syllabus syllabus);

	/**
	 * 获取一条Syllabus信息
	 * 
	 * @param syllabus_id
	 * @return
	 */
	public SyllabusView getSyllabus(String syllabus_id);

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
