package com.ncu.testbank.permission.data;

public class Authen {
	private String token;
	// 再次认证标识，默认是0。1为认证
	private boolean reAuth;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isReAuth() {
		return reAuth;
	}

	public void setReAuth(boolean reAuth) {
		this.reAuth = reAuth;
	}
}
