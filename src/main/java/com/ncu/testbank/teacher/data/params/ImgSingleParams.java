package com.ncu.testbank.teacher.data.params;

import com.ncu.testbank.teacher.data.Single;

public class ImgSingleParams {
	private long point_id;
	private String answer;
	private int level;

	public Single toSingle() {
		Single single = new Single();
		single.setPoint_id(point_id);
		single.setLevel(level);
		single.setAnswer(answer);
		return single;
	}

	public long getPoint_id() {
		return point_id;
	}

	public void setPoint_id(long point_id) {
		this.point_id = point_id;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
