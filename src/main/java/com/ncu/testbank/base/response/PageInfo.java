package com.ncu.testbank.base.response;

public class PageInfo {
	private int page = 1;  //当前页数
	private int rows = 15;  //每页显示条数
	private int total; //总数据量
	private int totalPage; //总页数
	public PageInfo(){
		
	}
	public PageInfo(int page, int rows) {
		super();
		this.page = page;
		this.rows = rows;
	}
	public int getStartRecord(){
		if((page-1)*rows>total){
			return rows;
		}else{
			return (page-1)*rows;
		}
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
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
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
}
