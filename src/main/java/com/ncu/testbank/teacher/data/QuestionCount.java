package com.ncu.testbank.teacher.data;

/**
 * 智能组卷所用题目难度等级对应题目数量POJO类
 * 
 * @author Jedeft
 * 
 */
public class QuestionCount {
	private int hardCount;
	private int mediumCount;
	private int easyCount;

	public int getHardCount() {
		return hardCount;
	}

	public void setHardCount(int hardCount) {
		this.hardCount = hardCount;
	}

	public int getMediumCount() {
		return mediumCount;
	}

	public void setMediumCount(int mediumCount) {
		this.mediumCount = mediumCount;
	}

	public int getEasyCount() {
		return easyCount;
	}

	public void setEasyCount(int easyCount) {
		this.easyCount = easyCount;
	}

	public QuestionCount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuestionCount(int hardCount, int mediumCount, int easyCount) {
		super();
		this.hardCount = hardCount;
		this.mediumCount = mediumCount;
		this.easyCount = easyCount;
	}

}
