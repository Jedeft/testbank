package com.ncu.testbank.permission.data;

/**
 * 登录，权限控制模块
 * @author Jedeft
 *
 */
public class Role {
	private long role_id;
	private String role_name;
	
	public long getRole_id() {
		return role_id;
	}
	public void setRole_id(long role_id) {
		this.role_id = role_id;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
}
