package com.ncu.testbank.admin.service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.ncu.testbank.admin.data.Teacher;
import com.ncu.testbank.base.response.PageInfo;

public interface IBankBuilderService {
	/**
	 * 检索题库建设者信息
	 * 
	 * @param page
	 * @param teacher
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	public List<Teacher> searchData(PageInfo page, Teacher teacher)
			throws IllegalAccessException, InvocationTargetException,
			IntrospectionException;

	/**
	 * 插入一个题库建设者
	 * 
	 * @param teacher_id
	 */
	public void insertOne(String teacher_id);

	/**
	 * 删除一个题库建设者
	 * 
	 * @param teacher_id
	 */
	public void deleteOne(String teacher_id);

	/**
	 * 批量删除题库建设者
	 * 
	 * @param teacher_id
	 */
	public void deleteData(List<String> teacher_id);

	/**
	 * 获取一个题库建设者
	 * 
	 * @param teacher_id
	 */
	public Teacher getBankBuilder(String teacher_id);
}
