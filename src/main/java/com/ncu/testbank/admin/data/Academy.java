package com.ncu.testbank.admin.data;

/**
 * 基础数据模块
 * 
 * @author Jedeft
 * 
 */
public class Academy {
	private String academy_id;
	private String name;

	public Academy() {
		super();
	}

	public Academy(String academy_id, String name) {
		super();
		this.academy_id = academy_id;
		this.name = name;
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
