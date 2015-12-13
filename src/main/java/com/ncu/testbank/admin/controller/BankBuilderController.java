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
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseMsg insertBankBuilder(@RequestBody Teacher teacher) {
		ResponseMsg msg = new ResponseMsg();
		try {
			bankBuilderService.insertOne(teacher.getTeacher_id());

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
	 * 删除bankBuilder
	 * 
	 * @param bankBuilder
	 * @return
	 */
	@RequiresRoles("rootAdmin")
	@RequestMapping(value = "/bankBuilders/{teacher_id}", method = RequestMethod.DELETE)
	public ResponseMsg deleteBankBuilder(@PathVariable String teacher_id) {
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
	public ResponseMsg deleteBankBuilders(
			@RequestBody Map<String, List<String>> map) {
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
	public ResponseMsg getBankBuilder(@PathVariable String teacher_id) {
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
	public ResponseQueryMsg searchData(PageInfo page, Teacher teacher) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			List<Teacher> teacherList;
			teacherList = bankBuilderService.searchData(page, teacher);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = teacherList;

			msg.total = page.getTotal();
			msg.totalPage = page.getTotalPage();
			msg.currentPage = page.getPage();
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
