package com.ncu.testbank.teacher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ncu.testbank.admin.data.Teacher;
import com.ncu.testbank.admin.service.ITeacherService;
import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.ResponseMsg;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "bankBuilderInfo-api", description = "查看题库建设者教师信息", position = 3)
@RestController("teacherController2")
@RequestMapping("/teacher")
public class TeacherController {
	
	@Autowired
	private ITeacherService teacherService;
	
	/**
	 * 根据id获取指定teacher
	 * 
	 * @param teacher
	 * @return
	 */
	@RequestMapping(value = "/teachers/{teacher_id}", method = RequestMethod.GET)
	@ApiOperation(value = "获取指定教师", httpMethod = "GET", response = ResponseMsg.class, notes = "需要baseTeacher权限，请header中携带Token")
	public ResponseMsg getTeacher(
			@ApiParam(required = true, name = "teacher_id", value = "教师ID") @PathVariable String teacher_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			Teacher data = teacherService.getTeacher(teacher_id);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = data;
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
