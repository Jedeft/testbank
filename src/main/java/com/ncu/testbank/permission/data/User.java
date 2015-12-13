package com.ncu.testbank.permission.data;

/**
 * 登录，权限控制模块
 * @author Jedeft
 *
 */
public class User {
	private String username;
	private String password;
	private String second_pwd;
	private String name;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSecond_pwd() {
		return second_pwd;
	}
	public void setSecond_pwd(String second_pwd) {
		this.second_pwd = second_pwd;
	}
}
