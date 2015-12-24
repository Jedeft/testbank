package com.ncu.testbank.admin.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ncu.testbank.admin.data.BankBuilder;
import com.ncu.testbank.admin.data.view.BankBuilderView;
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
			@ApiParam(required = true, name = "bankBuilder", value = "题库建设者json数据") @RequestBody BankBuilder bankBuilder) {
		ResponseMsg msg = new ResponseMsg();
		try {
			bankBuilderService.insertOne(bankBuilder);

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
	@RequestMapping(value = "/bankBuilders", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除题库建设人员", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要rootAdmin权限，请header中携带Token")
	public ResponseMsg deleteBankBuilder(
			@ApiParam(required = true, name = "bankBuilder", value = "题库建设人员json数据") @RequestBody BankBuilder bankBuilder) {
		ResponseMsg msg = new ResponseMsg();
		try {
			bankBuilderService.deleteOne(bankBuilder);
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
			@ApiParam(required = false, name = "academy_id", value = "学院ID信息检索") @RequestParam(value = "academy_id", required = false) String academy_id,
			@ApiParam(required = false, name = "teacher_id", value = "教师ID信息检索") @RequestParam(value = "teacher_id", required = false) String teacher_id,
			@ApiParam(required = false, name = "teacher_name", value = "教师名信息检索") @RequestParam(value = "teacher_name", required = false) String teacher_name,
			@ApiParam(required = false, name = "course_id", value = "课程ID信息检索") @RequestParam(value = "course_id", required = false) String course_id,
			@ApiParam(required = false, name = "course_name", value = "课程名信息检索") @RequestParam(value = "course_name", required = false) String course_name) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			List<BankBuilderView> bankBuilderList;
			BankBuilderView bankBuilder = new BankBuilderView(teacher_id,
					teacher_name, course_id, course_name, academy_id);
			PageInfo pageInfo = new PageInfo(page, rows);
			bankBuilderList = bankBuilderService.searchData(pageInfo,
					bankBuilder);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = bankBuilderList;

			msg.total = pageInfo.getTotal();
			msg.totalPage = pageInfo.getTotalPage();
			msg.currentPage = pageInfo.getPage();
			msg.pageCount = bankBuilderList.size();
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
