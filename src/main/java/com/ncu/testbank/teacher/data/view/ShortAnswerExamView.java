package com.ncu.testbank.teacher.data.view;

public class ShortAnswerExamView {
	private long exam_id;
	private long question_id;
	private String stuanswer;
	private String rightanswer;
	private double score;
	private String question;
	private int type;

	public long getExam_id() {
		return exam_id;
	}

	public void setExam_id(long exam_id) {
		this.exam_id = exam_id;
	}

	public long getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(long question_id) {
		this.question_id = question_id;
	}

	public String getStuanswer() {
		return stuanswer;
	}

	public void setStuanswer(String stuanswer) {
		this.stuanswer = stuanswer;
	}

	public String getRightanswer() {
		return rightanswer;
	}

	public void setRightanswer(String rightanswer) {
		this.rightanswer = rightanswer;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
