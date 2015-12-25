package com.ncu.testbank.teacher.data.params;

import java.util.List;

import com.ncu.testbank.teacher.data.Template;

public class TemplateParams {
	private long template_id;
	private String course_id;
	private String name;
	private int single_num;
	private int multiple_num;
	private int judge_num;
	private int shortAnswer_num;
	private int single_score;
	private int multiple_score;
	private int judge_score;
	private int shortAnswer_score;
	private int level;
	private double easy_ratio;
	private double medium_ratio;
	private double hard_ratio;
	// 考试类型：1.在线考试，2.笔试，3.练习
	private int type;
	private List<Long> point_id;

	public Template toTemplate() {
		Template template = new Template();

		template.setTemplate_id(template_id);
		template.setCourse_id(course_id);
		template.setName(name);
		template.setSingle_num(single_num);
		template.setMultiple_num(multiple_num);
		template.setJudge_num(judge_num);
		template.setShortAnswer_num(shortAnswer_num);
		template.setSingle_score(single_score);
		template.setMultiple_score(multiple_score);
		template.setJudge_score(judge_score);
		template.setShortAnswer_score(shortAnswer_score);
		template.setLevel(level);
		template.setEasy_ratio(easy_ratio);
		template.setMedium_ratio(medium_ratio);
		template.setHard_ratio(hard_ratio);
		template.setType(type);

		if (point_id != null) {
			String pointStr = point_id.toString();
			pointStr = pointStr.substring(1, pointStr.length() - 1).trim();
			template.setPoint_id(pointStr);
		}
		return template;
	}

	public long getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(long template_id) {
		this.template_id = template_id;
	}

	public String getCourse_id() {
		return course_id;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getShortAnswer_num() {
		return shortAnswer_num;
	}

	public void setShortAnswer_num(int shortAnswer_num) {
		this.shortAnswer_num = shortAnswer_num;
	}

	public int getSingle_score() {
		return single_score;
	}

	public void setSingle_score(int single_score) {
		this.single_score = single_score;
	}

	public int getMultiple_score() {
		return multiple_score;
	}

	public void setMultiple_score(int multiple_score) {
		this.multiple_score = multiple_score;
	}

	public int getJudge_score() {
		return judge_score;
	}

	public void setJudge_score(int judge_score) {
		this.judge_score = judge_score;
	}

	public int getShortAnswer_score() {
		return shortAnswer_score;
	}

	public void setShortAnswer_score(int shortAnswer_score) {
		this.shortAnswer_score = shortAnswer_score;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getEasy_ratio() {
		return easy_ratio;
	}

	public void setEasy_ratio(double easy_ratio) {
		this.easy_ratio = easy_ratio;
	}

	public double getMedium_ratio() {
		return medium_ratio;
	}

	public void setMedium_ratio(double medium_ratio) {
		this.medium_ratio = medium_ratio;
	}

	public double getHard_ratio() {
		return hard_ratio;
	}

	public void setHard_ratio(double hard_ratio) {
		this.hard_ratio = hard_ratio;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Long> getPoint_id() {
		return point_id;
	}

	public void setPoint_id(List<Long> point_id) {
		this.point_id = point_id;
	}

}
