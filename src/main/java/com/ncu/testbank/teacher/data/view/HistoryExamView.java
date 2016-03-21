package com.ncu.testbank.teacher.data.view;

import java.sql.Timestamp;

/**
 * 历史试卷信息（笔试）
 * 
 * @author Jedeft
 * 
 */
public class HistoryExamView {
	private long exam_id;
	private long template_id;
	private Timestamp start_time;
	private Timestamp end_time;
	private double score;
	private String user_id;
	private int level;
	private int single_num;
	private int multiple_num;
	private int judge_num;
	private int shortanswer_num;

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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSingle_num() {
		return single_num;
	}

	public void setSingle_num(int single_num) {
		this.single_num = single_num;
	}

	public int getMultiple_num() {
		return multiple_num;
	}

	public void setMultiple_num(int multiple_num) {
		this.multiple_num = multiple_num;
	}

	public int getJudge_num() {
		return judge_num;
	}

	public void setJudge_num(int judge_num) {
		this.judge_num = judge_num;
	}

	public int getShortanswer_num() {
		return shortanswer_num;
	}

	public void setShortanswer_num(int shortanswer_num) {
		this.shortanswer_num = shortanswer_num;
	}

}
