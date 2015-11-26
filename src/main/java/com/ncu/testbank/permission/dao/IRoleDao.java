package com.ncu.testbank.permission.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.permission.data.Role;

@Repository
public interface IRoleDao {
	
	/**
	 * 查询用户角色权限
	 * @param username
	 * @return
	 */
	public List<Role> searchRole(String username);
}
