package com.ncu.testbank.teacher.data.params;

import com.ncu.testbank.teacher.data.Point;

public class PointParams {
	private long point_id;
	private String name;
	
	public Point toPoint() {
		Point point = new Point();
		point.setName(name);
		point.setPoint_id(point_id);
		
		return point;
	}
	public long getPoint_id() {
		return point_id;
	}
	public void setPoint_id(long point_id) {
		this.point_id = point_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
