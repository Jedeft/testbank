package com.ncu.testbank.teacher.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.ncu.testbank.admin.data.Course;
import com.ncu.testbank.admin.service.ICourseService;
import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.base.response.ResponseQueryMsg;
import com.ncu.testbank.permission.data.User;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "teacherCourse-api", description = "教师端有关于课程的检索操作", position = 3)
@RestController("teacherCourse")
@RequestMapping("/teacher")
public class CourseController {

	@Autowired
	private ICourseService courseService;

	/**
	 * 通过教师ID检索带课course信息
	 * 
	 * @param course
	 * @return
	 */
	@RequestMapping(value = "/syllabus/course", method = RequestMethod.GET)
	@ApiOperation(value = "检索教师带课课程", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseTeacher权限，请header中携带Token")
	public ResponseMsg searchFromSyllabus(@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			List<Course> courseList = courseService
					.searchFromSyllabusByTID(user.getUsername());
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = courseList;
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
	 * 通过教师ID检索题库建设course信息
	 * 
	 * @param course
	 * @return
	 */
	@RequestMapping(value = "/bankBuilder/course", method = RequestMethod.GET)
	@ApiOperation(value = "检索教师题库建设课程", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseTeacher权限，请header中携带Token")
	public ResponseMsg searchFrombankBuilder(@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			List<Course> courseList = courseService
					.searchFromSyllabusByTID(user.getUsername());
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = courseList;
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
