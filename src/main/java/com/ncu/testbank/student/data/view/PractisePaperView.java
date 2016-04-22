package com.ncu.testbank.student.data.view;

import java.sql.Timestamp;
import java.util.List;

public class PractisePaperView {
	private long practise_id;
	private long template_id;
	private Timestamp start_time;
	private Timestamp end_time;
	private String user_id;
	// 由于不确定页面具体显示哪些题目信息，故用Map
	private List<SinglePractiseView> singleList;
	private List<MultiplePractiseView> multipleList;
	private List<JudgePractiseView> judgeleList;

	public PractisePaperView() {
		super();
	}

	public PractisePaperView(long practise_id, long template_id,
			Timestamp start_time, Timestamp end_time, String user_id) {
		super();
		this.practise_id = practise_id;
		this.template_id = template_id;
		this.start_time = start_time;
		this.end_time = end_time;
		this.user_id = user_id;
	}

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

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public List<SinglePractiseView> getSingleList() {
		return singleList;
	}

	public void setSingleList(List<SinglePractiseView> singleList) {
		this.singleList = singleList;
	}

	public List<MultiplePractiseView> getMultipleList() {
		return multipleList;
	}

	public void setMultipleList(List<MultiplePractiseView> multipleList) {
		this.multipleList = multipleList;
	}

	public List<JudgePractiseView> getJudgeleList() {
		return judgeleList;
	}

	public void setJudgeleList(List<JudgePractiseView> judgeleList) {
		this.judgeleList = judgeleList;
	}

}
