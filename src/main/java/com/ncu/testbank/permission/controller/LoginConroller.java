package com.ncu.testbank.permission.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.permission.data.Token;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.permission.service.IUserService;

@Controller
@RequestMapping("/permission")
public class LoginConroller {
	
	@Autowired
	private IUserService userService;
	
	@ResponseBody
	@RequestMapping("/login")
	public ResponseMsg login(User user) {
		ResponseMsg msg = new ResponseMsg();
        try {
        	Subject currUser = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),user.getPassword());
            token.setRememberMe(true);
        	currUser.login(token);
        	
        	//赋token值
        	Token jwt = userService.createToken(user.getUsername());
        	
        	msg.errorCode = ErrorCode.LOGIN_SUCCESS.code;
            msg.msg = ErrorCode.LOGIN_SUCCESS.name;
            msg.data = jwt;
        } catch (ShiroException e) {
        	ErrorCode error = e.getErrorCode();
        	msg.errorCode = error.code;
        	msg.msg = error.name;
        } catch (ServiceException e) {
        	ErrorCode error = e.getErrorCode();
        	msg.errorCode = error.code;
        	msg.msg = error.name;
        } catch (DaoException e) {
        	ErrorCode error = e.getErrorCode();
        	msg.errorCode = error.code;
        	msg.msg = error.name;
        }
        return msg;
	}
	
}
