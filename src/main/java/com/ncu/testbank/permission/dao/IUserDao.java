package com.ncu.testbank.permission.dao;

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

}
