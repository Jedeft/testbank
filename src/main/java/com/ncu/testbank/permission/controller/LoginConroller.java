package com.ncu.testbank.permission.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.permission.service.IUserService;

@Controller
@RequestMapping("/permission")
public class LoginConroller {
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/login")
	public void login(User user) {
		Subject currUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),user.getPassword());
        token.setRememberMe(true);
        try {
        	currUser.login(token);
        }catch (AuthenticationException e) {
        	System.out.println("登录失败错误信息:"+e);
            token.clear();
        }
	}
	
	
}
