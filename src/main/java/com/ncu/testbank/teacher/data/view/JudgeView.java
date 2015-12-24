package com.ncu.testbank.teacher.data.view;

import java.sql.Timestamp;

public class JudgeView {
	private long question_id;
	private long point_id;
	private String question;
	private String answer;
	private int type;
	private int level;
	private Timestamp create_time;
	private Timestamp modify_time;
	private String create_teacher_id;
	// 创建题目教师名
	private String create_teacher_name;
	private String modify_teacher_id;
	// 修改题目教师名
	private String modify_teacher_name;
	
	public long getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(long question_id) {
		this.question_id = question_id;
	}

	public long getPoint_id() {
		return point_id;
	}

	public void setPoint_id(long point_id) {
		this.point_id = point_id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public Timestamp getModify_time() {
		return modify_time;
	}

	public void setModify_time(Timestamp modify_time) {
		this.modify_time = modify_time;
	}

	public String getCreate_teacher_id() {
		return create_teacher_id;
	}

	public void setCreate_teacher_id(String create_teacher_id) {
		this.create_teacher_id = create_teacher_id;
	}

	public String getModify_teacher_id() {
		return modify_teacher_id;
	}

	public void setModify_teacher_id(String modify_teacher_id) {
		this.modify_teacher_id = modify_teacher_id;
	}

	public String getCreate_teacher_name() {
		return create_teacher_name;
	}

	public void setCreate_teacher_name(String create_teacher_name) {
		this.create_teacher_name = create_teacher_name;
	}

	public String getModify_teacher_name() {
		return modify_teacher_name;
	}

	public void setModify_teacher_name(String modify_teacher_name) {
		this.modify_teacher_name = modify_teacher_name;
	}

}
