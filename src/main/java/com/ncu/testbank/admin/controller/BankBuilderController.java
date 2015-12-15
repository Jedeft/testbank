package com.ncu.testbank.admin.controller;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ncu.testbank.admin.data.BankBuilder;
import com.ncu.testbank.admin.data.Teacher;
import com.ncu.testbank.admin.service.IBankBuilderService;
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

@Api(value = "bankBuilder-api", description = "有关于题库建设者的CURD操作", position = 2)
@RestController
@RequestMapping("/admin")
public class BankBuilderController {
	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private IBankBuilderService bankBuilderService;
	@Autowired
	private ITeacherService teacherService;

	/**
	 * 新增bankBuilder
	 * 
	 * @param bankBuilder
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/bankBuilders", method = RequestMethod.POST)
	@ApiOperation(value = "添加题库建设人员", httpMethod = "POST", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg insertBankBuilder(
			@ApiParam(required = true, name = "teacher_id", value = "教师IDjson数据") @RequestBody BankBuilder bankBuilder) {
		ResponseMsg msg = new ResponseMsg();
		try {
			bankBuilderService.insertOne(bankBuilder.getTeacher_id());

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = teacherService.getTeacher(bankBuilder.getTeacher_id());
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
	 * 删除bankBuilder
	 * 
	 * @param bankBuilder
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/bankBuilders/{teacher_id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除题库建设人员", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteBankBuilder(
			@ApiParam(required = true, name = "teacher_id", value = "教师ID") @PathVariable String teacher_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			bankBuilderService.deleteOne(teacher_id);
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
	 * 批量删除bankBuilder
	 * 
	 * @param bankBuilder
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/bankBuilders/batch", method = RequestMethod.DELETE)
	@ApiOperation(value = "批量删除题库建设人员", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteBankBuilders(
			@ApiParam(required = true, name = "teacher_id", value = "teacher_id数组json数据") @RequestBody Map<String, List<String>> map) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (map.get("teacher_id") != null) {
				bankBuilderService.deleteData(map.get("teacher_id"));
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
	 * 根据id获取指定bankBuilder
	 * 
	 * @param bankBuilder
	 * @return
	 */
	@RequestMapping(value = "/bankBuilders/{teacher_id}", method = RequestMethod.GET)
	@ApiOperation(value = "获取题库建设人员", httpMethod = "GET", response = ResponseMsg.class, notes = "需要baseAdmin权限，请header中携带Token")
	public ResponseMsg getBankBuilder(
			@ApiParam(required = true, name = "teacher_id", value = "教师ID") @PathVariable String teacher_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			Teacher data = bankBuilderService.getBankBuilder(teacher_id);

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
	 * 分页获取bankBuilder信息
	 * 
	 * @param bankBuilder
	 * @return
	 */
	@RequestMapping(value = "/bankBuilders", method = RequestMethod.GET)
	@ApiOperation(value = "检索题库建设人员", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseAdmin权限，请header中携带Token")
	public ResponseQueryMsg searchData(
			@ApiParam(required = true, name = "page", value = "分页数据") @RequestParam(value = "page", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "每页数据量") @RequestParam(value = "rows", required = true) Integer rows,
			@ApiParam(required = false, name = "teacher_id", value = "教师ID信息检索") @RequestParam(value = "teacher_id", required = false) String teacher_id,
			@ApiParam(required = false, name = "academy_id", value = "学院ID信息检索") @RequestParam(value = "academy_id", required = false) String academy_id,
			@ApiParam(required = false, name = "name", value = "教师名信息检索") @RequestParam(value = "name", required = false) String name) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			List<Teacher> teacherList;
			Teacher teacher = new Teacher(teacher_id, academy_id, name);
			PageInfo pageInfo = new PageInfo(page, rows);
			teacherList = bankBuilderService.searchData(pageInfo, teacher);

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
		} catch (IllegalAccessException | InvocationTargetException
				| IntrospectionException e) {
			msg.errorCode = ErrorCode.MAP_CONVERT_ERROR.code;
			msg.msg = ErrorCode.MAP_CONVERT_ERROR.name;
			log.error(e.getMessage());
		}
		return msg;
	}
}
