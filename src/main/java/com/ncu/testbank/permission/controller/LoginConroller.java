package com.ncu.testbank.permission.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.ResponsePermissionMsg;
import com.ncu.testbank.base.response.ResponseToken;
import com.ncu.testbank.permission.data.Authen;
import com.ncu.testbank.permission.data.Role;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.permission.service.IUserService;

@RestController
@RequestMapping("/permission")
public class LoginConroller {
	
	@Autowired
	private IUserService userService;
	/**
	 * 登录
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/json_web_token", method=RequestMethod.POST)
	public ResponseToken login(@RequestBody User user) {
		ResponseToken msg = new ResponseToken();
        try {
        	Subject currUser = SecurityUtils.getSubject();
        	Session session = currUser.getSession();
            UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),user.getPassword());
            token.setRememberMe(true);
        	currUser.login(token);
        	//赋token值
        	Authen authen = userService.createToken(user.getUsername());
        	List<Role> roleList = userService.searchRole(user.getUsername());
        	msg.errorCode = ErrorCode.LOGIN_SUCCESS.code;
            msg.msg = ErrorCode.LOGIN_SUCCESS.name;
            msg.token = authen.getToken();
            msg.role = roleList;
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
	
	/**
	 * 二次认证
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/twiceAuth", method=RequestMethod.POST)
	public ResponsePermissionMsg reAuth(@RequestBody User user) {
		ResponsePermissionMsg msg = new ResponsePermissionMsg();
        try {
        	userService.reAuth(user);
        	List<Role> roleList = userService.searchAllRole(user.getUsername());
        	msg.errorCode = ErrorCode.LOGIN_SUCCESS.code;
            msg.msg = ErrorCode.LOGIN_SUCCESS.name;
            msg.role = roleList;
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
