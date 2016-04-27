package com.ncu.testbank.teacher.data.params;

/**
 * 试卷批改参数
 */
public class CheckParams {
	private Long exam_id;
	private Long question_id;
	private Double score;

	public Long getExam_id() {
		return exam_id;
	}

	public void setExam_id(Long exam_id) {
		this.exam_id = exam_id;
	}

	public Long getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(Long question_id) {
		this.question_id = question_id;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

}
