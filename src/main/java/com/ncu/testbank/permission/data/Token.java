package com.ncu.testbank.permission.data;

public class Token {
	//用户token索引
	private String sub;
	private String token;
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
