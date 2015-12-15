package com.ncu.testbank.admin.data;

import com.ncu.testbank.base.response.PageInfo;

/**
 * 基础数据模块
 * 
 * @author Jedeft
 * 
 */
public class Teacher {
	private String teacher_id;
	private String academy_id;
	private String name;
	private String email;
	private String phone;
	private String qq;

	public Teacher() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Teacher(String teacher_id, String academy_id, String name) {
		super();
		this.teacher_id = teacher_id;
		this.academy_id = academy_id;
		this.name = name;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

}
