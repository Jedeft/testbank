package com.ncu.testbank.common.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.permission.data.TwiceAuthUser;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.permission.service.IUserService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "user-api", description = "修改密码", position = 1)
@RestController
@RequestMapping("/common")
public class UserController {

	@Autowired
	private IUserService userService;

	/**
	 * 修改一级密码
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/users/first", method = RequestMethod.PATCH)
	@ApiOperation(value = "修改密码", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要baseAdmin或者baseTeacher、baseStudent权限，请header中携带Token")
	public ResponseMsg changePwd(
			@ApiParam(required = true, name = "user", value = "账号信息json数据") @RequestBody User user) {
		ResponseMsg msg = new ResponseMsg();
		try {
			userService.updatePassword(user);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
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
	 * 修改二级密码
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/users/second", method = RequestMethod.PATCH)
	@ApiOperation(value = "修改二级密码", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg changeSecondPwd(
			@ApiParam(required = true, name = "user", value = "账号信息json数据") @RequestBody TwiceAuthUser twiceAuthUser) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = twiceAuthUser.toUser();
			userService.updatePassword(user);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
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
