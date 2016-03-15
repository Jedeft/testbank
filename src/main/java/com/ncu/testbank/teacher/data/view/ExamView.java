package com.ncu.testbank.teacher.data.view;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class ExamView {
	private long exam_id;
	private long template_id;
	private Timestamp start_time;
	private Timestamp end_time;
	private String user_id;
	// 由于不确定页面具体显示哪些题目信息，故用Map
	private List<Map<String, Object>> singleList;
	private List<Map<String, Object>> multipleList;
	private List<Map<String, Object>> judgeleList;
	private List<Map<String, Object>> shortAnswerleList;

	public ExamView() {
		super();
	}

	public ExamView(long exam_id, long template_id, Timestamp start_time,
			Timestamp end_time, String user_id) {
		super();
		this.exam_id = exam_id;
		this.template_id = template_id;
		this.start_time = start_time;
		this.end_time = end_time;
		this.user_id = user_id;
	}

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

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public List<Map<String, Object>> getSingleList() {
		return singleList;
	}

	public void setSingleList(List<Map<String, Object>> singleList) {
		this.singleList = singleList;
	}

	public List<Map<String, Object>> getMultipleList() {
		return multipleList;
	}

	public void setMultipleList(List<Map<String, Object>> multipleList) {
		this.multipleList = multipleList;
	}

	public List<Map<String, Object>> getJudgeleList() {
		return judgeleList;
	}

	public void setJudgeleList(List<Map<String, Object>> judgeleList) {
		this.judgeleList = judgeleList;
	}

	public List<Map<String, Object>> getShortAnswerleList() {
		return shortAnswerleList;
	}

	public void setShortAnswerleList(List<Map<String, Object>> shortAnswerleList) {
		this.shortAnswerleList = shortAnswerleList;
	}

}
