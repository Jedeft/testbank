package com.ncu.testbank.teacher.data;

import java.sql.Timestamp;

public class Exam {
	private long exam_id;
	private long template_id;
	private Timestamp start_time;
	private Timestamp end_time;
	private double score;
	private String user_id;
	private int status;

	public Exam() {
		super();
	}

	public Exam(long exam_id, long template_id, Timestamp start_time,
			Timestamp end_time, String user_id) {
		super();
		this.exam_id = exam_id;
		this.template_id = template_id;
		this.start_time = start_time;
		this.end_time = end_time;
		this.user_id = user_id;
	}

	public long getExam_id() {
		return exam_id;
	}

	public void setExam_id(long exam_id) {
		this.exam_id = exam_id;
	}

	public long getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(long template_id) {
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

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
