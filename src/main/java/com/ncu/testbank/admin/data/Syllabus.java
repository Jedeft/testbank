package com.ncu.testbank.admin.data;

import java.sql.Date;
import java.sql.Timestamp;

import com.ncu.testbank.base.response.PageInfo;

/**
 * 基础数据模块
 * 
 * @author Jedeft
 * 
 */
public class Syllabus {
	private String syllabus_id;
	private String teacher_id;
	private String course_id;
	private Date start;
	private Date end;

	public Syllabus() {
		super();
	}

	public Syllabus(Date start, Date end) {
		super();
		this.start = start;
		this.end = end;
	}

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

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

}
