package com.ncu.testbank.teacher.data;

/**
 * 智能组卷所用题目难度等级POJO类
 * 
 * @author Jedeft
 * 
 */
public class QuestionLevel {
	private int hard;
	private int medium;
	private int easy;

	public int getHard() {
		return hard;
	}

	public void setHard(int hard) {
		this.hard = hard;
	}

	public int getMedium() {
		return medium;
	}

	public void setMedium(int medium) {
		this.medium = medium;
	}

	public int getEasy() {
		return easy;
	}

	public void setEasy(int easy) {
		this.easy = easy;
	}

	public QuestionLevel() {
		super();
	}

	public QuestionLevel(int hard, int medium, int easy) {
		super();
		this.hard = hard;
		this.medium = medium;
		this.easy = easy;
	}

}
