package com.ncu.testbank.permission.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.permission.data.Role;

@Repository
public interface IRoleDao {

	/**
	 * 查询用户角色（无需二级认证的角色）
	 * 
	 * @param username
	 * @return
	 */
	public List<Role> searchRole(String username);

	/**
	 * 查询用户角色权限（全部角色，包括二级认证的角色）
	 * 
	 * @param username
	 * @return
	 */
	public List<Role> searchAllRole(String username);
	
	/**
	 * 赋予用户角色
	 * @param params(用户名username，角色role_id，ID等级level)
	 * @return
	 */
	public int putRole(Map<String, Object> params);
	
	/**
	 * 删除用户角色
	 * @param params(username, role_id)
	 * @return
	 */
	public int deleteRole(Map<String, Object> params);
}
