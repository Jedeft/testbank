package com.ncu.testbank.student.data.params;

public class PractiseParams {
	private Long template_id;
	// 练习时间(小时)
	private Integer hour;
	// 练习时间(分钟)
	private Integer minute;

	public Long getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(Long template_id) {
		this.template_id = template_id;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

}
