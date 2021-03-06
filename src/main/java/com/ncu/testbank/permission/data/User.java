package com.ncu.testbank.permission.data;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 登录模块
 * 
 * @author Jedeft
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@ApiModel(value = "loginAccount")
public class User {
	private String username;
	private String password;
	@ApiModelProperty(hidden = true)
	private String second_pwd;
	@ApiModelProperty(hidden = true)
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
