package com.ncu.testbank.admin.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.data.Student;
import com.ncu.testbank.admin.service.IStudentService;
import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.base.response.ResponseQueryMsg;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "student-api", description = "有关于学生的CURD操作", position = 2)
@RestController
@RequestMapping("/admin")
public class StudentController {

	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private IStudentService studentService;

	/**
	 * 新增student
	 * 
	 * @param student
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/students", method = RequestMethod.POST)
	@ApiOperation(value = "添加学生", httpMethod = "POST", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg insertStudent(
			@ApiParam(required = true, name = "student", value = "学生信息json数据") @RequestBody Student student) {
		ResponseMsg msg = new ResponseMsg();
		try {
			studentService.insertOne(student);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = student;
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
	 * 更新student
	 * 
	 * @param student
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/students", method = RequestMethod.PATCH)
	@ApiOperation(value = "更新学生", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg updateStudent(
			@ApiParam(required = true, name = "student", value = "学生信息json数据") @RequestBody Student student) {
		ResponseMsg msg = new ResponseMsg();
		try {

			studentService.updateOne(student);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = studentService.getStudent(student.getStudent_id());
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
	 * 删除student
	 * 
	 * @param student
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/students/{student_id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除学生", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteStudent(
			@ApiParam(required = true, name = "student_id", value = "学生ID") @PathVariable String student_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			studentService.deleteOne(student_id);

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
	 * 批量删除student
	 * 
	 * @param student
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/students/batch", method = RequestMethod.DELETE)
	@ApiOperation(value = "批量删除学生", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteStudents(
			@ApiParam(required = true, name = "student_id", value = "student_id数组json数据") @RequestBody Map<String, List<String>> map) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (map.get("student_id") != null && !map.get("student_id").equals("")) {
				studentService.deleteData(map.get("student_id"));
			} else {
				msg.errorCode = 66666;
				msg.msg = "请选择删除学生！";
			}
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
	 * 根据id获取指定student
	 * 
	 * @param student
	 * @return
	 */
	@RequestMapping(value = "/students/{student_id}", method = RequestMethod.GET)
	@ApiOperation(value = "获取指定学生", httpMethod = "GET", response = ResponseMsg.class, notes = "需要baseAdmin权限，请header中携带Token")
	public ResponseMsg getStudent(
			@ApiParam(required = true, name = "student_id", value = "学生ID") @PathVariable String student_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			Student data = studentService.getStudent(student_id);

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

	/**
	 * 分页获取student信息
	 * 
	 * @param student
	 * @return
	 */
	@RequestMapping(value = "/students", method = RequestMethod.GET)
	@ApiOperation(value = "检索学生", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseAdmin权限，请header中携带Token")
	public ResponseQueryMsg searchData(
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
		} catch (IllegalAccessException | InstantiationException
				| InvocationTargetException | IntrospectionException e) {
			msg.errorCode = ErrorCode.MAP_CONVERT_ERROR.code;
			msg.msg = ErrorCode.MAP_CONVERT_ERROR.name;
			log.error(e.getMessage());
		}
		return msg;
	}

	/**
	 * csv文件批量录入student信息
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/students/csv", method = RequestMethod.POST)
	@ApiOperation(value = "批量导入学生", httpMethod = "POST", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg loadCsv(
			@ApiParam(required = true, name = "file", value = "csv文件") @RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request) {
		ResponseMsg msg = new ResponseMsg();
		try {

			String fileName = new Date().getTime() + "_"
					+ file.getOriginalFilename();
			String path = request.getSession().getServletContext()
					.getRealPath("upload");

			if (!fileName.endsWith(".csv")) {
				msg.errorCode = ErrorCode.FILE_TYPE_ERROR.code;
				msg.msg = ErrorCode.FILE_TYPE_ERROR.name;
				return msg;
			}
			studentService.loadCsv(fileName, path, file);
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
		} catch (IOException e) {
			msg.errorCode = ErrorCode.FILE_IO_ERROR.code;
			msg.msg = ErrorCode.FILE_IO_ERROR.name;
			log.error(e.getMessage());
		} catch (IllegalStateException e) {
			msg.errorCode = ErrorCode.FILE_IO_ERROR.code;
			msg.msg = ErrorCode.FILE_IO_ERROR.name;
			log.error(e.getMessage());
		}
		return msg;
	}
}
