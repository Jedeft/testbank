package com.ncu.testbank.base.response;

public class ResponseMsg {
	//error code : 0 :success ; non 0， error
	public int errorCode = 0;
	
	//returning message
	public String msg;

	public Object data;
}
