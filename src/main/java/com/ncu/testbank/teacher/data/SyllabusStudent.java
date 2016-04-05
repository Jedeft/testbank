package com.ncu.testbank.teacher.data;

public class SyllabusStudent {
	private String syllabus_id;
	private String student_id;

	public SyllabusStudent() {
		super();
	}

	public SyllabusStudent(String syllabus_id, String student_id) {
		super();
		this.syllabus_id = syllabus_id;
		this.student_id = student_id;
	}

	public String getSyllabus_id() {
		return syllabus_id;
	}

	public void setSyllabus_id(String syllabus_id) {
		this.syllabus_id = syllabus_id;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

}
