package com.ncu.testbank.permission.dao;

import java.util.List;

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
	 * 
	 * @param user
	 * @return
	 */
	public int insertOne(User user);

	/**
	 * 修改用户
	 * 
	 * @param user
	 * @return
	 */
	public int updateOne(User user);

	/**
	 * 批量录入用户
	 * 
	 * @param file
	 */
	public void loadCsv(String file);

	/**
	 * 删除用户
	 * 
	 * @param username
	 * @return
	 */
	public int deleteOne(String username);

	/**
	 * 批量删除用户
	 * 
	 * @param username
	 */
	public void deleteData(List<String> username);

	/**
	 * 获取批量用户
	 * 
	 * @param username
	 * @return
	 */
	public List<User> getUsers(List<String> username);
}
