package com.ncu.testbank.student.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.data.Template;
import com.ncu.testbank.teacher.data.params.TemplateParams;
import com.ncu.testbank.teacher.service.ITemplateService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "practiseTemplate-api", description = "练习模板CRUD操作", position = 3)
@RestController("practiseTemplate")
@RequestMapping("/student")
public class TemplateController {
	@Autowired
	private ITemplateService templateService;

	/**
	 * 新增练习模板
	 * 
	 * @param template
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/templates", method = RequestMethod.POST)
	@ApiOperation(value = "添加练习模板", httpMethod = "POST", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg insertStudents(
			@ApiParam(required = true, name = "template", value = "添加的练习模板json数据，ID为后台自生成，前台校验三个ratio比例加起来为100%，type默认为3，可无这个参数，学生端模板没有简答题。") @RequestBody TemplateParams template,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			//此处默认为3模板
			template.setType(3);
			//练习模板没有简答题
			template.setShortAnswer_num(0);
			template.setShortAnswer_score(0);
			Template data = templateService.insertOne(template, user);
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
	 * 修改练习模板
	 * 
	 * @param template
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/templates", method = RequestMethod.PATCH)
	@ApiOperation(value = "修改练习模板", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg updateWriting(
			@ApiParam(required = true, name = "template", value = "修改的模板json数据，前台校验三个ratio比例加起来为100%，学生端模板没有简答题。") @RequestBody TemplateParams template) {
		ResponseMsg msg = new ResponseMsg();
		try {
			//练习模板没有简答题
			template.setShortAnswer_num(0);
			template.setShortAnswer_score(0);
			templateService.updateOne(template);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = templateService.getOne(template.getTemplate_id());
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
	 * 获取模板
	 * 
	 * @param template_id
	 * @return
	 */
	@RequestMapping(value = "/templates/{template_id}", method = RequestMethod.GET)
	@ApiOperation(value = "获取模板", httpMethod = "GET", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg getSingles(
			@ApiParam(required = true, name = "template_id", value = "模板ID") @PathVariable long template_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			Template template = templateService.getOne(template_id);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = template;
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
	 * 根据课程ID检索模板
	 * 
	 * @param course_id
	 * @return
	 */
	@RequestMapping(value = "/templates", method = RequestMethod.GET)
	@ApiOperation(value = "根据课程ID检索模板", httpMethod = "GET", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg searchPoints(
			@ApiParam(required = true, name = "course_id", value = "课程ID") @RequestParam(value = "course_id", required = true) String course_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			List<Template> list = templateService.searchData(course_id, 3);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = list;
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
	 * 删除模板
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/templates", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除模板", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg deleteTemplates(
			@ApiParam(required = true, name = "template_id", value = "删除模板json数组，集合中为template_id数组") @RequestBody Map<String, List<Long>> map) {
		ResponseMsg msg = new ResponseMsg();
		try {
			List<Long> params = null;
			if (map != null && map.get("tempalte_id") != null) {
				params = map.get("tempalte_id");
			} else {
				msg.errorCode = 66666;
				msg.msg = "请选择删除模板！";
				return msg;
			}
			templateService.deleteData(params);
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
