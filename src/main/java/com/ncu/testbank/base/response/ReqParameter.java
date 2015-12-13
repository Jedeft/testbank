package com.ncu.testbank.base.response;

public class ReqParameter {
	private int rows = 15; // 每页显示条数
	private int page = 1;
	private String params = null;

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getSearch_condition() {
		return params;
	}

	public void setSearch_condition(String params) {
		this.params = params;
	}
}
