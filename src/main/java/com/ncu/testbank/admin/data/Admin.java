package com.ncu.testbank.admin.data;

/**
 * 基础数据模块
 * @author Jedeft
 *
 */
public class Admin {
	private String admin_id;
	private String academy_id;
	private String name;
	
	public String getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}
	public String getAcademy_id() {
		return academy_id;
	}
	public void setAcademy_id(String academy_id) {
		this.academy_id = academy_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
