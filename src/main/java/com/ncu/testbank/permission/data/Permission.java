package com.ncu.testbank.permission.data;

/**
 * 登录，权限控制模块
 * 
 * @author Jedeft
 * 
 */
public class Permission {
	private long permission_id;
	private String name;
	private long pid;
	private int level;

	public long getPermission_id() {
		return permission_id;
	}

	public void setPermission_id(long permission_id) {
		this.permission_id = permission_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
