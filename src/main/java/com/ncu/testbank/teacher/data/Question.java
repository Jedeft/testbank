package com.ncu.testbank.teacher.data;

/**
 * 智能组卷POJO类
 * 
 * @author Jedeft
 * 
 */
public class Question {
	private long question_id;
	private long point_id;
	private int level;

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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
