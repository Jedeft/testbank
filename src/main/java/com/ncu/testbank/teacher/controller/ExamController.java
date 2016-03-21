package com.ncu.testbank.teacher.controller;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresRoles;
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
import com.ncu.testbank.base.utils.ActiveMqUtils;
import com.ncu.testbank.base.utils.JSONUtils;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.data.Exam;
import com.ncu.testbank.teacher.data.Template;
import com.ncu.testbank.teacher.data.params.ExamParams;
import com.ncu.testbank.teacher.data.params.ExamQuestionParams;
import com.ncu.testbank.teacher.data.view.ExamView;
import com.ncu.testbank.teacher.service.IExamService;
import com.ncu.testbank.teacher.service.IJudgeService;
import com.ncu.testbank.teacher.service.IMultipleService;
import com.ncu.testbank.teacher.service.IShortAnswerService;
import com.ncu.testbank.teacher.service.ISingleService;
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

	@Autowired
	private ISingleService singleService;

	@Autowired
	private IMultipleService multipleService;

	@Autowired
	private IJudgeService judgeService;

	@Autowired
	private IShortAnswerService shortAnswerService;

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
			@ApiParam(required = true, name = "examParams", value = "考试参数json数据，若为人工组卷的笔试，则student_id为空，teacher_id可忽略") @RequestBody ExamParams examParams,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			// 若有学生ID，那说明为在线考试，则走消息队列进行异步后台组卷
			if (examParams.getStudent_id() != null
					&& examParams.getStudent_id().size() > 0) {
				examParams.setTeacher_id(user.getUsername());
				String json = JSONUtils.convertObject2Json(examParams);
				ActiveMqUtils.sendMessage(json);
				return msg;
			}
			Template template = templateService.getOne(examParams
					.getTemplate_id());
			Exam exam = examService.createExam(template, user.getUsername(),
					examParams.getStart_time(), examParams.getEnd_time());
			// 获取exam试卷信息返回前台
			ExamView examView = new ExamView(exam.getExam_id(),
					exam.getTemplate_id(), exam.getStart_time(),
					exam.getEnd_time(), user.getUsername());
			if (template.getSingle_num() > 0) {
				examView.setSingleList(singleService
						.searchExamSingleNoAnswer(exam.getExam_id()));
			}
			if (template.getMultiple_num() > 0) {
				examView.setMultipleList(multipleService
						.searchExamMultipleNoAnswer(exam.getExam_id()));
			}
			if (template.getJudge_num() > 0) {
				examView.setJudgeleList(judgeService
						.searchExamJudgeNoAnswer(exam.getExam_id()));
			}
			if (template.getShortAnswer_num() > 0) {
				examView.setShortAnswerleList(shortAnswerService
						.searchExamShortNoAnswer(exam.getExam_id()));
			}
			msg.data = examView;
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
	 * 删除单选题目
	 * 
	 * @param question
	 * @return
	 */
	@RequestMapping(value = "/exam/singles", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除考试单选题目", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要teacher权限，请header中携带Token")
	public ResponseMsg deleteSingle(
			@ApiParam(required = true, name = "examQusetion", value = "删除题目json数据") @RequestBody ExamQuestionParams examQuestion) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (examQuestion != null && examQuestion.getExam_id() != null
					&& examQuestion.getQuestion_id() != null) {
				examService.deleteSingle(examQuestion.getExam_id(),
						examQuestion.getQuestion_id());
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
	 * 删除多选题目
	 * 
	 * @param question
	 * @return
	 */
	@RequestMapping(value = "/exam/multiples", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除考试多选题目", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要teacher权限，请header中携带Token")
	public ResponseMsg deleteMultiple(
			@ApiParam(required = true, name = "examQusetion", value = "删除题目json数据") @RequestBody ExamQuestionParams examQuestion) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (examQuestion != null && examQuestion.getExam_id() != null
					&& examQuestion.getQuestion_id() != null) {
				examService.deleteMultiple(examQuestion.getExam_id(),
						examQuestion.getQuestion_id());
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
	 * 删除判断题目
	 * 
	 * @param question
	 * @return
	 */
	@RequestMapping(value = "/exam/judges", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除考试判断题目", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要teacher权限，请header中携带Token")
	public ResponseMsg deleteJudge(
			@ApiParam(required = true, name = "examQusetion", value = "删除题目json数据") @RequestBody ExamQuestionParams examQuestion) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (examQuestion != null && examQuestion.getExam_id() != null
					&& examQuestion.getQuestion_id() != null) {
				examService.deleteJudge(examQuestion.getExam_id(),
						examQuestion.getQuestion_id());
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
	 * 删除简答题目
	 * 
	 * @param question
	 * @return
	 */
	@RequestMapping(value = "/exam/shortAnswers", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除考试简答题目", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要teacher权限，请header中携带Token")
	public ResponseMsg deleteShortAnswer(
			@ApiParam(required = true, name = "examQusetion", value = "删除题目json数据") @RequestBody ExamQuestionParams examQuestion) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (examQuestion != null && examQuestion.getExam_id() != null
					&& examQuestion.getQuestion_id() != null) {
				examService.deleteShortAnswer(examQuestion.getExam_id(),
						examQuestion.getQuestion_id());
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
	 * 添加考试单选题
	 * 
	 * @param judge
	 * @param session
	 * @return
	 */
	@RequiresRoles("bankBuilder")
	@RequestMapping(value = "/exam/singles", method = RequestMethod.POST)
	@ApiOperation(value = "添加考试单选题", httpMethod = "POST", response = ResponseMsg.class, notes = "需要teacher权限，请header中携带Token")
	public ResponseMsg insertSingle(
			@ApiParam(required = true, name = "examQuestion", value = "添加题目json数据") @RequestBody ExamQuestionParams examQuestion) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (examQuestion != null && examQuestion.getExam_id() != null
					&& examQuestion.getQuestion_id() != null) {
				msg.data = examService.insertSingle(examQuestion.getExam_id(),
						examQuestion.getQuestion_id());
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
	 * 添加考试多选题
	 * 
	 * @param judge
	 * @param session
	 * @return
	 */
	@RequiresRoles("bankBuilder")
	@RequestMapping(value = "/exam/multiples", method = RequestMethod.POST)
	@ApiOperation(value = "添加考试多选题", httpMethod = "POST", response = ResponseMsg.class, notes = "需要teacher权限，请header中携带Token")
	public ResponseMsg insertMultiple(
			@ApiParam(required = true, name = "examQuestion", value = "添加题目json数据") @RequestBody ExamQuestionParams examQuestion) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (examQuestion != null && examQuestion.getExam_id() != null
					&& examQuestion.getQuestion_id() != null) {
				msg.data = examService.insertMultiple(
						examQuestion.getExam_id(),
						examQuestion.getQuestion_id());
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
	 * 添加考试判断题
	 * 
	 * @param judge
	 * @param session
	 * @return
	 */
	@RequiresRoles("bankBuilder")
	@RequestMapping(value = "/exam/judges", method = RequestMethod.POST)
	@ApiOperation(value = "添加考试判断题", httpMethod = "POST", response = ResponseMsg.class, notes = "需要teacher权限，请header中携带Token")
	public ResponseMsg insertJudge(
			@ApiParam(required = true, name = "examQuestion", value = "添加题目json数据") @RequestBody ExamQuestionParams examQuestion) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (examQuestion != null && examQuestion.getExam_id() != null
					&& examQuestion.getQuestion_id() != null) {
				msg.data = examService.insertJudge(examQuestion.getExam_id(),
						examQuestion.getQuestion_id());
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
	 * 添加考试简答题
	 * 
	 * @param judge
	 * @param session
	 * @return
	 */
	@RequiresRoles("bankBuilder")
	@RequestMapping(value = "/exam/shortAnswer", method = RequestMethod.POST)
	@ApiOperation(value = "添加考试简答题", httpMethod = "POST", response = ResponseMsg.class, notes = "需要teacher权限，请header中携带Token")
	public ResponseMsg insertShortAnswer(
			@ApiParam(required = true, name = "examQuestion", value = "添加题目json数据") @RequestBody ExamQuestionParams examQuestion) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (examQuestion != null && examQuestion.getExam_id() != null
					&& examQuestion.getQuestion_id() != null) {
				msg.data = examService.insertShortAnswer(
						examQuestion.getExam_id(),
						examQuestion.getQuestion_id());
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
}
