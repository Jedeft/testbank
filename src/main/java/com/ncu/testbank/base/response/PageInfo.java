package com.ncu.testbank.base.response;

public class PageInfo {
	private int page;
	private int rows;  //每页显示条数
	private int total;
	public PageInfo(){
		
	}
	public PageInfo(int page, int rows) {
		super();
		this.page = page;
		this.rows = rows;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getStartRecord(){
		if((page-1)*rows>total){
			return rows;
		}else{
			return (page-1)*rows;
		}
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
