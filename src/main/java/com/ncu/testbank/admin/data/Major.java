package com.ncu.testbank.admin.data;

import com.ncu.testbank.base.response.PageInfo;

/**
 * 基础数据模块
 * 
 * @author Jedeft
 * 
 */
public class Major {
	private String major_id;
	private String academy_id;
	private String name;

	public String getMajor_id() {
		return major_id;
	}

	public void setMajor_id(String major_id) {
		this.major_id = major_id;
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
