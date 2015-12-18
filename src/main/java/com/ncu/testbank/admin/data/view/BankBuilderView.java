package com.ncu.testbank.admin.data.view;

public class BankBuilderView {
	private String teacher_id;
	private String teacher_name;
	private String course_id;
	private String course_name;
	private String academy_id;

	public BankBuilderView() {
		super();
	}

	public BankBuilderView(String teacher_id, String teacher_name,
			String course_id, String course_name, String academy_id) {
		super();
		this.teacher_id = teacher_id;
		this.teacher_name = teacher_name;
		this.course_id = course_id;
		this.course_name = course_name;
		this.academy_id = academy_id;
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

	public String getAcademy_id() {
		return academy_id;
	}

	public void setAcademy_id(String academy_id) {
		this.academy_id = academy_id;
	}

}
