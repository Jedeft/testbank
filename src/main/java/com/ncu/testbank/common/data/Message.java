package com.ncu.testbank.common.data;

import java.sql.Timestamp;

public class Message {
	private long message_id;
	private long title_id;
	private String title;
	private String message;
	private String send_id;
	private String receive_id;
	private Timestamp send_time;
	private Timestamp receive_time;
	private String flag;

	public Message() {
		super();
	}

	public Message(Long title_id, String send_id, String receive_id, String flag) {
		super();
		if ( title_id != null ) {
			this.title_id = title_id;
		}
		this.send_id = send_id;
		this.receive_id = receive_id;
		this.flag = flag;
	}

	public long getMessage_id() {
		return message_id;
	}

	public void setMessage_id(long message_id) {
		this.message_id = message_id;
	}

	public String getSend_id() {
		return send_id;
	}

	public void setSend_id(String send_id) {
		this.send_id = send_id;
	}

	public String getReceive_id() {
		return receive_id;
	}

	public void setReceive_id(String receive_id) {
		this.receive_id = receive_id;
	}

	public Timestamp getSend_time() {
		return send_time;
	}

	public void setSend_time(Timestamp send_time) {
		this.send_time = send_time;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public long getTitle_id() {
		return title_id;
	}

	public void setTitle_id(long title_id) {
		this.title_id = title_id;
	}

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

	public Timestamp getReceive_time() {
		return receive_time;
	}

	public void setReceive_time(Timestamp receive_time) {
		this.receive_time = receive_time;
	}

}
