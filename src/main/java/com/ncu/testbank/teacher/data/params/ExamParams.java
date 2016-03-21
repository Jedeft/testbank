package com.ncu.testbank.teacher.data.params;

import java.sql.Timestamp;
import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;

@ApiModel
public class ExamParams {
	private Long template_id;
	private Timestamp start_time;
	private Timestamp end_time;
	// 若为在线考试，那么该学生集合有值。
	private List<String> student_id;
	// 创建考试的教师ID
	private String teacher_id;

	public Long getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(Long template_id) {
		this.template_id = template_id;
	}

	public Timestamp getStart_time() {
		return start_time;
	}

	public void setStart_time(Timestamp start_time) {
		this.start_time = start_time;
	}

	public Timestamp getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Timestamp end_time) {
		this.end_time = end_time;
	}

	public List<String> getStudent_id() {
		return student_id;
	}

	public void setStudent_id(List<String> student_id) {
		this.student_id = student_id;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}

}
