package com.ncu.testbank.teacher.data.view;

import java.sql.Date;

/**
 * 授课学生视图，关联syllabus, student, class表
 * 
 * @author Jedeft
 * 
 */
public class TeachingStudentView {
	private String syllabus_id;
	private String course_id;
	private String student_id;
	private String student_name;
	private String class_id;
	private String class_name;
	// 授课开始时间
	private Date start;
	// 授课结束时间
	private Date end;

	public TeachingStudentView() {
		super();
	}

	public TeachingStudentView(String course_id, String student_id,
			String student_name) {
		super();
		this.course_id = course_id;
		this.student_id = student_id;
		this.student_name = student_name;
	}

	public String getSyllabus_id() {
		return syllabus_id;
	}

	public void setSyllabus_id(String syllabus_id) {
		this.syllabus_id = syllabus_id;
	}

	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public String getClass_id() {
		return class_id;
	}

	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
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
