package com.ncu.testbank.permission.data;

/**
 * 登录，权限控制模块
 * @author Jedeft
 *
 */
public class Permission {
	private long permission_id;
	private String name;
	private String url;
	private String icon;
	private long pid;
	
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
}
