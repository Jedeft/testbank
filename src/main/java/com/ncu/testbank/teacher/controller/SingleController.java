package com.ncu.testbank.teacher.controller;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.ncu.testbank.admin.data.view.BankBuilderView;
import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.base.response.ResponseQueryMsg;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.data.Single;
import com.ncu.testbank.teacher.data.view.SingleView;
import com.ncu.testbank.teacher.service.ISingleService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "single-api", description = "单选题CRUD", position = 3)
@RestController
@RequestMapping("/teacher")
public class SingleController {
	
	private Logger log = Logger.getLogger("testbankLog");
	
	@Autowired
	private ISingleService singleService;
	
	@RequiresRoles("bankBuilder")
	@RequestMapping(value = "/singles/writing", method = RequestMethod.POST)
	@ApiOperation(value = "添加文字单选题", httpMethod = "POST", response = ResponseMsg.class, notes = "需要bankBuilder权限，请header中携带Token")
	public ResponseMsg insertWriting(
			@ApiParam(required = true, name = "single", value = "添加的单选题json数据，ID为后台生成") @RequestBody Single single,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			singleService.insertWriting(single, user);
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
	
	@RequiresRoles("bankBuilder")
	@RequestMapping(value = "/singles/writing", method = RequestMethod.PATCH)
	@ApiOperation(value = "修改文字单选题", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要bankBuilder权限，请header中携带Token")
	public ResponseMsg updateWriting(
			@ApiParam(required = true, name = "single", value = "修改的单选题json数据") @RequestBody Single single,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			singleService.updateWriting(single, user);
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
	 * 分页获取single单选题信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/singles", method = RequestMethod.GET)
	@ApiOperation(value = "检索单选题目", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseTeacher权限，请header中携带Token")
	public ResponseQueryMsg searchData(
			@ApiParam(required = true, name = "page", value = "分页数据") @RequestParam(value = "page", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "每页数据量") @RequestParam(value = "rows", required = true) Integer rows,
			@ApiParam(required = true, name = "point_id", value = "考点ID信息检索") @RequestParam(value = "point_id", required = true) Long point_id,
			@ApiParam(required = false, name = "type", value = "题目类型检索检索(1表示文字题目，2表示图片题目)") @RequestParam(value = "type", required = false) Integer type,
			@ApiParam(required = false, name = "level", value = "题目难度信息检索(1-5)") @RequestParam(value = "level", required = false) Integer level,
			@ApiParam(required = false, name = "question_id", value = "题目ID信息检索") @RequestParam(value = "question_id", required = false) Long question_id) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			List<SingleView> singleList;
			PageInfo pageInfo = new PageInfo(page, rows);
			Single single = new Single(question_id, point_id, type, level);
			singleList = singleService.searchData(pageInfo, single);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = singleList;

			msg.total = pageInfo.getTotal();
			msg.totalPage = pageInfo.getTotalPage();
			msg.currentPage = pageInfo.getPage();
			msg.pageCount = singleList.size();
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
