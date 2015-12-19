package com.ncu.testbank.teacher.data.params;

import java.util.List;
import java.util.Map;

/**
 * 授课学生管理模块前台传来的params集合
 * 
 * @author Jedeft
 * 
 */
public class TeachingStudentParams {
	
	private String course_id;
	// key : student_id
	private List<String> student_id;

	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public List<String> getStudent_id() {
		return student_id;
	}

	public void setStudent_id(List<String> student_id) {
		this.student_id = student_id;
	}

}
