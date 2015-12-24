package com.ncu.testbank.teacher.data;

import java.sql.Timestamp;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel("multiple")
public class Multiple {
	private long question_id;
	private long point_id;
	private String question;
	private String A;
	private String B;
	private String C;
	private String D;
	private String answer;
	@ApiModelProperty(hidden = true)
	private int type;
	private int level;
	@ApiModelProperty(hidden = true)
	private Timestamp create_time;
	@ApiModelProperty(hidden = true)
	private Timestamp modify_time;
	@ApiModelProperty(hidden = true)
	private String create_teacher_id;
	@ApiModelProperty(hidden = true)
	private String modify_teacher_id;

	public Multiple() {
		super();
	}

	public Multiple(Long question_id, Long point_id, Integer type, Integer level) {
		super();
		if (question_id != null) {
			this.question_id = question_id;
		}
		if (point_id != null) {
			this.point_id = point_id;
		}
		if (type != null) {
			this.type = type;
		}
		if (level != null) {
			this.level = level;
		}
	}

	public Multiple(Long point_id, String answer, Integer type, Integer level) {
		super();
		if (point_id != null) {
			this.point_id = point_id;
		}
		if (answer != null) {
			this.answer = answer;
		}
		if (type != null) {
			this.type = type;
		}
		if (level != null) {
			this.level = level;
		}
	}

	public long getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(long question_id) {
		this.question_id = question_id;
	}

	public long getPoint_id() {
		return point_id;
	}

	public void setPoint_id(long point_id) {
		this.point_id = point_id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getA() {
		return A;
	}

	public void setA(String a) {
		A = a;
	}

	public String getB() {
		return B;
	}

	public void setB(String b) {
		B = b;
	}

	public String getC() {
		return C;
	}

	public void setC(String c) {
		C = c;
	}

	public String getD() {
		return D;
	}

	public void setD(String d) {
		D = d;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public Timestamp getModify_time() {
		return modify_time;
	}

	public void setModify_time(Timestamp modify_time) {
		this.modify_time = modify_time;
	}

	public String getCreate_teacher_id() {
		return create_teacher_id;
	}

	public void setCreate_teacher_id(String create_teacher_id) {
		this.create_teacher_id = create_teacher_id;
	}

	public String getModify_teacher_id() {
		return modify_teacher_id;
	}

	public void setModify_teacher_id(String modify_teacher_id) {
		this.modify_teacher_id = modify_teacher_id;
	}

}
