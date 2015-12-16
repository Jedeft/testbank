package com.ncu.testbank.admin.data;

import com.ncu.testbank.base.response.PageInfo;

/**
 * 基础数据模块
 * 
 * @author Jedeft
 * 
 */
public class Student {
	private String student_id;
	private String class_id;
	private String name;

	public Student() {
		super();
	}

	public Student(String student_id, String class_id, String name) {
		super();
		this.student_id = student_id;
		this.class_id = class_id;
		this.name = name;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getClass_id() {
		return class_id;
	}

	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
