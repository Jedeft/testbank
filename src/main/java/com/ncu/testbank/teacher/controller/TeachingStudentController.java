package com.ncu.testbank.teacher.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.data.params.TeachingStudentParams;
import com.ncu.testbank.teacher.service.ITeachingStudentService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "teachingStudents-api", description = "授课学生管理操作", position = 3)
@RestController
@RequestMapping("/teacher")
public class TeachingStudentController {

	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private ITeachingStudentService teachingStudentService;

	/**
	 * 新增授课学生
	 * 
	 * @param academy
	 * @return
	 */
	@RequestMapping(value = "/teachingStudents", method = RequestMethod.POST)
	@ApiOperation(value = "添加授课学生", httpMethod = "POST", response = ResponseMsg.class, notes = "需要baseTeacher权限，请header中携带Token")
	public ResponseMsg insertAcademy(
			@ApiParam(required = true, name = "academy", value = "添加的授课学生json数据，集合中为student_id数组, 添加的教师ID为当前用户ID") @RequestBody TeachingStudentParams teachingSParams,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			teachingStudentService.insertStudents(user.getUsername(),
					teachingSParams.getCourse_id(), teachingSParams.getStudent_id());
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
