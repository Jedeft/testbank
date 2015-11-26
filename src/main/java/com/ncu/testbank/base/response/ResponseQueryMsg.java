package com.ncu.testbank.base.response;

public class ResponseQueryMsg extends ResponseMsg{
	public Object rows;
	
	//totals of returning records 
	public int total;
	
	//pages in total 
	public int total_pages;
	
	//current page position
	public int current_page;
	
	//number records of current page
	public int page_count;	
}
