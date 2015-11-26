package com.ncu.testbank.permission.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.permission.data.Permission;

@Repository
public interface IPermissionDao {
	public List<Permission> searchPermission(long role_id);
}
