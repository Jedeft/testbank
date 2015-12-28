package com.ncu.testbank.admin.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.data.Teacher;
import com.ncu.testbank.admin.service.ITeacherService;
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

@Api(value = "teacher-api", description = "有关于教师的CURD操作", position = 2)
@RestController
@RequestMapping("/admin")
public class TeacherController {

	@Autowired
	private ITeacherService teacherService;

	/**
	 * 新增teacher
	 * 
	 * @param teacher
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/teachers", method = RequestMethod.POST)
	@ApiOperation(value = "添加教师", httpMethod = "POST", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg insertTeacher(
			@ApiParam(required = true, name = "teacher", value = "教师信息json数据") @RequestBody Teacher teacher) {
		ResponseMsg msg = new ResponseMsg();
		try {
			teacherService.insertOne(teacher);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = teacher;
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
	 * 更新teacher
	 * 
	 * @param teacher
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/teachers", method = RequestMethod.PATCH)
	@ApiOperation(value = "更新教师", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg updateTeacher(
			@ApiParam(required = true, name = "teacher", value = "教师信息json数据") @RequestBody Teacher teacher) {
		ResponseMsg msg = new ResponseMsg();
		try {

			teacherService.updateOne(teacher);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = teacherService.getTeacher(teacher.getTeacher_id());
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
	 * 删除teacher
	 * 
	 * @param teacher
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/teachers/{teacher_id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除教师", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteTeacher(
			@ApiParam(required = true, name = "teacher_id", value = "教师ID") @PathVariable String teacher_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			teacherService.deleteOne(teacher_id);

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
	 * 批量删除teacher
	 * 
	 * @param teacher
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/teachers/batch", method = RequestMethod.DELETE)
	@ApiOperation(value = "批量删除教师", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteTeachers(
			@ApiParam(required = true, name = "teacher_id", value = "teacher_id数组json数据") @RequestBody Map<String, List<String>> map) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (map.get("teacher_id") != null
					&& !map.get("teacher_id").equals("")) {
				teacherService.deleteData(map.get("teacher_id"));
			} else {
				msg.errorCode = 66666;
				msg.msg = "请选择删除教师！";
				return msg;
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
	 * 根据id获取指定teacher
	 * 
	 * @param teacher
	 * @return
	 */
	@RequestMapping(value = "/teachers/{teacher_id}", method = RequestMethod.GET)
	@ApiOperation(value = "获取指定教师", httpMethod = "GET", response = ResponseMsg.class, notes = "需要baseAdmin权限，请header中携带Token")
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

	/**
	 * 分页获取teacher信息
	 * 
	 * @param teacher
	 * @return
	 */
	@RequestMapping(value = "/teachers", method = RequestMethod.GET)
	@ApiOperation(value = "检索教师", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseAdmin权限，请header中携带Token")
	public ResponseQueryMsg searchData(
			@ApiParam(required = true, name = "page", value = "分页数据") @RequestParam(value = "page", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "每页数据量") @RequestParam(value = "rows", required = true) Integer rows,
			@ApiParam(required = false, name = "teacher_id", value = "教师ID信息检索") @RequestParam(value = "teacher_id", required = false) String teacher_id,
			@ApiParam(required = false, name = "academy_id", value = "学院ID信息检索") @RequestParam(value = "academy_id", required = false) String academy_id,
			@ApiParam(required = false, name = "name", value = "教师姓名信息检索") @RequestParam(value = "name", required = false) String name) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			List<Teacher> teacherList;

			PageInfo pageInfo = new PageInfo(page, rows);
			Teacher teacher = new Teacher(teacher_id, academy_id, name);
			teacherList = teacherService.searchData(pageInfo, teacher);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = teacherList;

			msg.total = pageInfo.getTotal();
			msg.totalPage = pageInfo.getTotalPage();
			msg.currentPage = pageInfo.getPage();
			msg.pageCount = teacherList.size();
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
	 * csv文件批量录入teacher信息
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/teachers/csv", method = RequestMethod.POST)
	@ApiOperation(value = "批量导入教师", httpMethod = "POST", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
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
			teacherService.loadCsv(fileName, path, file);
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
