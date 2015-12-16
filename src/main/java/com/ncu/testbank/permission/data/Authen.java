package com.ncu.testbank.permission.data;

/**
 * 二级认证模块
 * @author Jedeft
 *
 */
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
