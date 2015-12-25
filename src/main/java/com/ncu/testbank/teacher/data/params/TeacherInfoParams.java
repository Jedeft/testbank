package com.ncu.testbank.teacher.data.params;

import com.ncu.testbank.admin.data.Teacher;

public class TeacherInfoParams {
	private String teacher_id;
	private String name;
	private String email;
	private String phone;
	private String qq;

	public Teacher toTeacher() {
		Teacher teacher = new Teacher();
		teacher.setEmail(email);
		teacher.setName(name);
		teacher.setPhone(phone);
		teacher.setQq(qq);
		teacher.setTeacher_id(teacher_id);
		return teacher;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
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
