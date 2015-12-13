package com.ncu.testbank.base.response;

public class ResponseQueryMsg extends ResponseMsg{
	//totals of returning records 
	public int total;
	
	//pages in total 
	public int totalPage;
	
	//current page position
	public int currentPage;
	
	//number records of current page
	public int pageCount;	
}
