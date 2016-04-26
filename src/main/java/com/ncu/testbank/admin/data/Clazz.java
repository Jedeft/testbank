package com.ncu.testbank.admin.data;


/**
 * 基础数据模块
 * 
 * @author Jedeft
 * 
 */
public class Clazz {
	private String class_id;
	private String major_id;
	private String name;

	public Clazz() {
		super();
	}

	public Clazz(String class_id, String major_id, String name) {
		super();
		this.class_id = class_id;
		this.major_id = major_id;
		this.name = name;
	}

	public String getClass_id() {
		return class_id;
	}

	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}

	public String getMajor_id() {
		return major_id;
	}

	public void setMajor_id(String major_id) {
		this.major_id = major_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
