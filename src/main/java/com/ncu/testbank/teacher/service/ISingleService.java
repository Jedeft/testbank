package com.ncu.testbank.teacher.service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.data.Single;
import com.ncu.testbank.teacher.data.view.SingleView;

public interface ISingleService {
	/**
	 * 插入文字题目
	 * 
	 * @param single
	 * @param user
	 *            当前操作用户信息
	 */
	public void insertWriting(Single single, User user);

	/**
	 * 修改文字题目
	 * 
	 * @param single
	 * @param user
	 */
	public void updateWriting(Single single, User user);

	/**
	 * 检索题目
	 * @param pageInfo
	 * @param single
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	public List<SingleView> searchData(PageInfo pageInfo, Single single)
			throws IllegalAccessException, InvocationTargetException,
			IntrospectionException;
	
	
}
