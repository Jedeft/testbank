package com.ncu.testbank.admin.service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.ncu.testbank.admin.data.BankBuilder;
import com.ncu.testbank.admin.data.Teacher;
import com.ncu.testbank.admin.data.view.BankBuilderView;
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
	public List<BankBuilderView> searchData(PageInfo page, BankBuilderView bankBuilder)
			throws IllegalAccessException, InvocationTargetException,
			IntrospectionException;

	/**
	 * 插入一个题库建设者
	 * 
	 * @param teacher_id
	 */
	public void insertOne(BankBuilder bankBuilder);

	/**
	 * 删除一个题库建设者
	 * 
	 * @param teacher_id
	 */
	public void deleteOne(BankBuilder bankBuilder);
}
