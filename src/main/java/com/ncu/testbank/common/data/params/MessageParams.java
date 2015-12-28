package com.ncu.testbank.common.data.params;

import java.util.List;

public class MessageParams {
	//主题
	private String title;
	//通知体
	private String message;
	//发送对象
	private List<String> receive_id;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getReceive_id() {
		return receive_id;
	}

	public void setReceive_id(List<String> receive_id) {
		this.receive_id = receive_id;
	}

}
