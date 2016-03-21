package com.ncu.testbank.teacher.data.view;

import java.sql.Timestamp;

/**
 * 学生在线考试情况视图
 * 
 * @author Jedeft
 * 
 */
public class OnlineExamView {
	private long exam_id;
	private long template_id;
	private Timestamp start_time;
	private Timestamp end_time;
	private double score;
	private String student_id;
	private String student_name;
	private int level;

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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
