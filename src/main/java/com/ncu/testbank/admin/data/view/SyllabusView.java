package com.ncu.testbank.admin.data.view;

/**
 * 视图POJO
 * 
 * @author Jedeft
 * 
 */
public class SyllabusView {
	private String syllabus_id;
	private String teacher_id;
	private String teacher_name;
	private String course_id;
	private String course_name;
	// 开始时间
	private String start;
	// 结束时间
	private String end;

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

	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
}
