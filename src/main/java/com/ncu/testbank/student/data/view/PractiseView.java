package com.ncu.testbank.student.data.view;

import java.sql.Timestamp;

public class PractiseView {
	private long practise_id;
	private long template_id;
	private Timestamp start_time;
	private Timestamp end_time;
	private double right_ratio;
	private int level;

	public long getPractise_id() {
		return practise_id;
	}

	public void setPractise_id(long practise_id) {
		this.practise_id = practise_id;
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

	public double getRight_ratio() {
		return right_ratio;
	}

	public void setRight_ratio(double right_ratio) {
		this.right_ratio = right_ratio;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}