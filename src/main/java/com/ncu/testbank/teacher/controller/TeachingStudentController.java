package com.ncu.testbank.teacher.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.ncu.testbank.admin.data.Student;
import com.ncu.testbank.admin.service.IStudentService;
import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.base.response.ResponseQueryMsg;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.data.params.TeachingStudentParams;
import com.ncu.testbank.teacher.data.view.TeachingStudentView;
import com.ncu.testbank.teacher.service.ITeachingStudentService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "teachingStudents-api", description = "授课学生管理操作", position = 3)
@RestController
@RequestMapping("/teacher")
public class TeachingStudentController {

	@Autowired
	private ITeachingStudentService teachingStudentService;

	@Autowired
	private IStudentService studentService;

	/**
	 * 新增授课学生
	 * 
	 * @param teachingSParams
	 * @return
	 */
	@RequestMapping(value = "/teachingStudents", method = RequestMethod.POST)
	@ApiOperation(value = "添加授课学生", httpMethod = "POST", response = ResponseMsg.class, notes = "需要baseTeacher权限，请header中携带Token")
	public ResponseMsg insertStudents(
			@ApiParam(required = true, name = "academy", value = "添加的授课学生json数据，集合中为student_id数组, 添加的教师ID为当前用户ID") @RequestBody TeachingStudentParams teachingSParams,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			teachingStudentService.insertStudents(user.getUsername(),
					teachingSParams.getCourse_id(),
					teachingSParams.getStudent_id());
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
	 * 分页获取student信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/students", method = RequestMethod.GET)
	@ApiOperation(value = "检索学生", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseTeacher权限，请header中携带Token")
	public ResponseQueryMsg searchStudents(
			@ApiParam(required = true, name = "page", value = "分页数据") @RequestParam(value = "page", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "每页数据量") @RequestParam(value = "rows", required = true) Integer rows,
			@ApiParam(required = false, name = "student_id", value = "学生ID信息检索") @RequestParam(value = "student_id", required = false) String student_id,
			@ApiParam(required = false, name = "class_id", value = "班级ID信息检索") @RequestParam(value = "class_id", required = false) String class_id,
			@ApiParam(required = false, name = "name", value = "学生姓名信息检索") @RequestParam(value = "name", required = false) String name) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			List<Student> studentList;
			PageInfo pageInfo = new PageInfo(page, rows);
			Student student = new Student(student_id, class_id, name);
			studentList = studentService.searchData(pageInfo, student);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = studentList;

			msg.total = pageInfo.getTotal();
			msg.totalPage = pageInfo.getTotalPage();
			msg.currentPage = pageInfo.getPage();
			msg.pageCount = studentList.size();
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
	 * 分页获取授课学生信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/teachingStudents", method = RequestMethod.GET)
	@ApiOperation(value = "检索授课学生", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseTeacher权限，请header中携带Token")
	public ResponseQueryMsg searchData(
			@ApiParam(required = true, name = "page", value = "分页数据") @RequestParam(value = "page", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "每页数据量") @RequestParam(value = "rows", required = true) Integer rows,
			@ApiParam(required = true, name = "course_id", value = "课程ID信息检索") @RequestParam(value = "course_id", required = true) String course_id,
			@ApiParam(required = false, name = "student_id", value = "学生ID信息检索") @RequestParam(value = "student_id", required = false) String student_id,
			@ApiParam(required = false, name = "student_name", value = "学生姓名信息检索") @RequestParam(value = "student_name", required = false) String student_name,
			@ApiIgnore HttpSession session) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			PageInfo pageInfo = new PageInfo(page, rows);
			TeachingStudentView teachingStudentView = new TeachingStudentView(
					course_id, student_id, student_name);

			User user = (User) session.getAttribute("currentUser");
			List<TeachingStudentView> list = teachingStudentService.searchData(
					pageInfo, teachingStudentView, user.getUsername());

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = list;

			msg.total = pageInfo.getTotal();
			msg.totalPage = pageInfo.getTotalPage();
			msg.currentPage = pageInfo.getPage();
			msg.pageCount = list.size();
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
	 * 
	 * @param teachingSParams
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/teachingStudents", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除授课学生", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要baseTeacher权限，请header中携带Token")
	public ResponseMsg deleteStudents(
			@ApiParam(required = true, name = "academy", value = "删除的授课学生json数据，集合中为student_id数组, 添加的教师ID为当前用户ID") @RequestBody TeachingStudentParams teachingSParams,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			teachingStudentService.deleteStudents(user.getUsername(),
					teachingSParams.getCourse_id(),
					teachingSParams.getStudent_id());
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
