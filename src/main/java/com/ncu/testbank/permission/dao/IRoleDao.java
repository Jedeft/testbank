package com.ncu.testbank.permission.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.permission.data.Role;

@Repository
public interface IRoleDao {
	
	/**
	 * 查询用户角色（无需二级认证的角色）
	 * @param username
	 * @return
	 */
	public List<Role> searchRole(String username);
	
	
	/**
	 * 查询用户角色权限（全部角色，包括二级认证的角色）
	 * @param username
	 * @return
	 */
	public List<Role> searchAllRole(String username);
}
