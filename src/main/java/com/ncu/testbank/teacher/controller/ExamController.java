package com.ncu.testbank.teacher.controller;

import javax.servlet.http.HttpSession;

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
import com.ncu.testbank.teacher.data.Template;
import com.ncu.testbank.teacher.data.params.ExamParams;
import com.ncu.testbank.teacher.service.IExamService;
import com.ncu.testbank.teacher.service.ITemplateService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "exam-api", description = "考试模块", position = 3)
@RestController
@RequestMapping("/teacher")
public class ExamController {

	@Autowired
	private IExamService examService;

	@Autowired
	private ITemplateService templateService;

	/**
	 * 根据模板生成考试试卷
	 * 
	 * @param examParams
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/exam", method = RequestMethod.POST)
	@ApiOperation(value = "根据模板生成考试试卷", httpMethod = "POST", response = ResponseMsg.class, notes = "需要teacher权限，请header中携带Token")
	public ResponseMsg insertWriting(
			@ApiParam(required = true, name = "examParams", value = "考试参数json数据，若为人工组卷的笔试，则student_id为空") @RequestBody ExamParams examParams,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			Template template = templateService.getOne(examParams
					.getTemplate_id());
			// 若有学生ID，那说明为在线考试，则走消息队列进行异步后台组卷
			if (examParams.getStudent_id() != null
					&& examParams.getStudent_id().size() > 0) {

			}
			Long exam_id = examService.createExam(template.getTemplate_id(),
					user.getUsername(), examParams.getStart_time(),
					examParams.getEnd_time());
			// TODO 获取exam试卷信息返回前台

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
