package com.ncu.testbank.admin.data;

import java.sql.Timestamp;

/**
 * 基础数据模块
 * @author Jedeft
 *
 */
public class Syllabus {
	private String syllabus_id;
	private String teacher_id;
	private String course_id;
	private Timestamp start;
	private Timestamp end;
	
	public String getSyllabus_id() {
		return syllabus_id;
	}
	public void setSyllabus_id(String syllabus_id) {
		this.syllabus_id = syllabus_id;
	}
	public String getTeacher_id() {
		return teacher_id;
	}
	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}
	public String getCourse_id() {
		return course_id;
	}
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}
	public Timestamp getStart() {
		return start;
	}
	public void setStart(Timestamp start) {
		this.start = start;
	}
	public Timestamp getEnd() {
		return end;
	}
	public void setEnd(Timestamp end) {
		this.end = end;
	}
}
