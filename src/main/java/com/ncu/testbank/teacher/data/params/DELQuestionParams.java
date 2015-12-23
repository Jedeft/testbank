package com.ncu.testbank.teacher.data.params;

/**
 * 删除题目参数POJO
 * 
 * @author Jedeft
 * 
 */
public class DELQuestionParams {
	private long question_id;
	// 题目类型
	private int type;

	public DELQuestionParams() {
		super();
	}

	public DELQuestionParams(long question_id, int type) {
		super();
		this.question_id = question_id;
		this.type = type;
	}

	public long getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(long question_id) {
		this.question_id = question_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
