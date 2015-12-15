package com.ncu.testbank.permission.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ncu.testbank.permission.data.User;

@Repository
public interface IUserDao {

	/**
	 * 获取用户信息
	 * 
	 * @param username
	 *            ： 用户名
	 * @return
	 */
	public User getUser(String username);

	/**
	 * 插入用户
	 * @param user
	 * @return
	 */
	public int insertOne(User user);
	
	/**
	 * 批量录入用户
	 * @param file
	 */
	public void loadCsv(String file);
}
