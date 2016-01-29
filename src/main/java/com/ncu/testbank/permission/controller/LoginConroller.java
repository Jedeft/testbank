package com.ncu.testbank.permission.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.base.response.ResponsePermissionMsg;
import com.ncu.testbank.base.response.ResponseToken;
import com.ncu.testbank.permission.data.Authen;
import com.ncu.testbank.permission.data.Role;
import com.ncu.testbank.permission.data.TwiceAuthUser;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.permission.service.IUserService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "permission-api", description = "登录、二级认证", position = 1)
@RestController
@RequestMapping("/permission")
public class LoginConroller {

	@Autowired
	private IUserService userService;

	/**
	 * 登录
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/json_web_token", method = RequestMethod.POST)
	@ApiOperation(value = "登录", httpMethod = "POST", response = ResponseToken.class, notes = "游客权限，登录成功errorCode=10000")
	public ResponseToken login(
			@ApiParam(required = true, name = "user", value = "用户名密码json数据") @RequestBody User user) {
		ResponseToken msg = new ResponseToken();
		try {
			Subject currUser = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(
					user.getUsername(), user.getPassword());
//			token.setRememberMe(true);
			currUser.login(token);
			// 赋token值
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
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/twiceAuth", method = RequestMethod.POST)
	@ApiOperation(value = "二级认证", httpMethod = "POST", response = ResponsePermissionMsg.class, notes = "需要baseAdmin权限，请header中携带Token,二级认证成功errorCode=20000")
	public ResponsePermissionMsg reAuth(
			@ApiParam(required = true, name = "user", value = "用户名二级密码json数据") @RequestBody TwiceAuthUser twiceAuthUser) {
		ResponsePermissionMsg msg = new ResponsePermissionMsg();
		try {
			User user = twiceAuthUser.toUser();
			userService.reAuth(user);
			List<Role> roleList = userService.searchAllRole(user.getUsername());
			msg.errorCode = ErrorCode.REAUTHEN_SUCCESS.code;
			msg.msg = ErrorCode.REAUTHEN_SUCCESS.name;
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
	 * 登出
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "//{username}", method = RequestMethod.DELETE)
	@ApiOperation(value = "登出", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要baseAdmin权限，请header中携带Token,登出成功errorCode=10010")
	public ResponseMsg logout(
			@ApiParam(required = true, name = "username", value = "用户名") @PathVariable String username,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (null == username || username.equals("")) {
				msg.errorCode = ErrorCode.USERNAME_MISSING.code;
				msg.msg = ErrorCode.USERNAME_MISSING.name;
				return msg;
			}
			
			userService.logout(username);
			// 删除session中的用户信息
			session.removeAttribute("currentUser");
			msg.errorCode = ErrorCode.LOGOUT_SUCCESS.code;
			msg.msg = ErrorCode.LOGOUT_SUCCESS.name;
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
