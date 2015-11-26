package com.ncu.testbank.admin.data;

/**
 * 基础数据模块
 * @author Jedeft
 *
 */
public class Course {
	private String course_id;
	private String major_id;
	private String name;
	
	public String getCourse_id() {
		return course_id;
	}
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
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
