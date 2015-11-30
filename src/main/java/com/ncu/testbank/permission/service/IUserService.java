package com.ncu.testbank.permission.service;

import java.util.List;

import com.ncu.testbank.permission.data.Permission;
import com.ncu.testbank.permission.data.Role;
import com.ncu.testbank.permission.data.Token;
import com.ncu.testbank.permission.data.User;

public interface IUserService {

	/**
	 * 登录
	 * @param user
	 * @return
	 */
	public boolean login(User user);
	
	/**
	 * 获取用户角色
	 * @param username
	 * @return
	 */
	public List<Role> searchRole(String username);
	
	/**
	 * 获取角色权限
	 * @param role_id
	 * @return
	 */
	public List<Permission> searchPermission(long role_id);
	
	
	/**
	 * 获取用户详细信息
	 * @param username
	 * @return
	 */
	public User getUser(String username);
	
	/**
	 * 生成Token
	 * @param username
	 * @return
	 */
	public Token createToken(String username);
	
	/**
	 * 验证Token
	 * @param username
	 * @param token
	 * @return
	 */
	public boolean validateToken(String token, String username);
}
