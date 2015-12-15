package com.ncu.testbank.permission.data;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 二次权限认证模块（此处和User作为两个POJO是迫于swagger框架的无奈）
 * 
 * @author Jedeft
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@ApiModel(value = "TiwceAuthAccount")
public class TwiceAuthUser {
	private String username;
	@ApiModelProperty(hidden = true)
	private String password;
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

	public String getSecond_pwd() {
		return second_pwd;
	}

	public void setSecond_pwd(String second_pwd) {
		this.second_pwd = second_pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User toUser() {
		User user = new User();

		user.setUsername(username);
		user.setPassword(password);
		user.setSecond_pwd(second_pwd);
		user.setName(name);

		return user;
	}
}
