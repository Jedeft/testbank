package com.ncu.testbank.student.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.base.response.ResponseQueryMsg;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.student.data.params.BatchStudentAnswerParams;
import com.ncu.testbank.student.data.params.QuestionParams;
import com.ncu.testbank.student.data.params.StudentAnswerParams;
import com.ncu.testbank.teacher.data.Exam;
import com.ncu.testbank.teacher.data.view.ExamPaperView;
import com.ncu.testbank.teacher.data.view.OnlineExamView;
import com.ncu.testbank.teacher.service.IExamService;
import com.ncu.testbank.teacher.service.IJudgeService;
import com.ncu.testbank.teacher.service.IMultipleService;
import com.ncu.testbank.teacher.service.IShortAnswerService;
import com.ncu.testbank.teacher.service.ISingleService;
import com.ncu.testbank.teacher.service.ITemplateService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "studentExam-api", description = "在线考试模块", position = 4)
@RestController("studentExam")
@RequestMapping("/student")
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
	 * 分页获取在线考试学生考试情况信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/exam/online", method = RequestMethod.GET)
	@ApiOperation(value = "检索在线考试学生考试情况信息", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseQueryMsg searchOnlineExam(
			@ApiParam(required = true, name = "page", value = "分页数据") @RequestParam(value = "page", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "每页数据量") @RequestParam(value = "rows", required = true) Integer rows,
			@ApiIgnore HttpSession session) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			PageInfo pageInfo = new PageInfo(page, rows);
			User user = (User) session.getAttribute("currentUser");
			List<OnlineExamView> examList = examService.searchOnlineBySID(user
					.getUsername());

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = examList;

			msg.total = pageInfo.getTotal();
			msg.totalPage = pageInfo.getTotalPage();
			msg.currentPage = pageInfo.getPage();
			msg.pageCount = examList != null ? examList.size() : 0;
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
	 * 获取在线考试试卷信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/exam/detail", method = RequestMethod.GET)
	@ApiOperation(value = "查看考试试卷详细信息（包含题目），若为当前正在考试试卷，结果集不包含正确答案", httpMethod = "GET", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg getExamDetail(
			@ApiParam(required = true, name = "exam_id", value = "试卷ID") @RequestParam(value = "exam_id", required = true) Long exam_id,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			Exam exam = examService.getExamByID(exam_id);
			User user = (User) session.getAttribute("currentUser");
			if (!user.getUsername().equals(exam.getUser_id())) {
				msg.errorCode = 83054;
				msg.msg = "学生只可以查看自己的试卷，请重试";
				return msg;
			}
			Date now = new Date();
			ExamPaperView examView = null;
			if (exam.getStart_time().getTime() < now.getTime()) {
				msg.errorCode = 99635;
				msg.msg = "考试还未开始，不能查看试卷";
				return msg;
			} else if (exam.getEnd_time().getTime() < now.getTime()) {
				// 考试结束，结果集包含正确答案
				examView = examService.getExamDetailByID(exam_id);
			} else {
				// 考试未结束，结果集不包含正确答案
				examView = examService.getExamDetailByIDNoAnswer(exam_id);
			}
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
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
	 * 提交单选题答案
	 * 
	 * @return
	 */
	@RequestMapping(value = "/exam/single", method = RequestMethod.PATCH)
	@ApiOperation(value = "提交单选题答案", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg updateSingleStuAnswer(
			@ApiParam(required = true, name = "studentAnswer", value = "学生答案json数据") @RequestBody StudentAnswerParams studentAnswer,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			//判断试卷是否已经提交过
			Exam exam = examService.getExamByID(studentAnswer.getTest_id());
			if ( exam.getExam_id() == 1 ) {
				msg.errorCode = ErrorCode.EXAM_COMPLETENESS.code;
				msg.msg = ErrorCode.EXAM_COMPLETENESS.name;
			}
			singleService.updateExamStuAnswer(studentAnswer.getTest_id(),
					studentAnswer.getShortAnswerParams().getQuestion_id(),
					studentAnswer.getShortAnswerParams().getAnswer());
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
	 * 提交多选题答案
	 * 
	 * @return
	 */
	@RequestMapping(value = "/exam/multiple", method = RequestMethod.PATCH)
	@ApiOperation(value = "提交多选题答案，多选答案用逗号分隔", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg updateMultipleStuAnswer(
			@ApiParam(required = true, name = "studentAnswer", value = "学生答案json数据") @RequestBody StudentAnswerParams studentAnswer,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			//判断试卷是否已经提交过
			Exam exam = examService.getExamByID(studentAnswer.getTest_id());
			if ( exam.getExam_id() == 1 ) {
				msg.errorCode = ErrorCode.EXAM_COMPLETENESS.code;
				msg.msg = ErrorCode.EXAM_COMPLETENESS.name;
			}
			multipleService.updateExamStuAnswer(studentAnswer.getTest_id(),
					studentAnswer.getShortAnswerParams().getQuestion_id(),
					studentAnswer.getShortAnswerParams().getAnswer());
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
	 * 提交判断题答案
	 * 
	 * @return
	 */
	@RequestMapping(value = "/exam/judge", method = RequestMethod.PATCH)
	@ApiOperation(value = "提交判断题答案", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg updateJudgeStuAnswer(
			@ApiParam(required = true, name = "studentAnswer", value = "学生答案json数据") @RequestBody StudentAnswerParams studentAnswer,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			//判断试卷是否已经提交过
			Exam exam = examService.getExamByID(studentAnswer.getTest_id());
			if ( exam.getExam_id() == 1 ) {
				msg.errorCode = ErrorCode.EXAM_COMPLETENESS.code;
				msg.msg = ErrorCode.EXAM_COMPLETENESS.name;
			}
			judgeService.updateExamStuAnswer(studentAnswer.getTest_id(),
					studentAnswer.getShortAnswerParams().getQuestion_id(),
					studentAnswer.getShortAnswerParams().getAnswer());
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
	 * 提交简答题答案
	 * 
	 * @return
	 */
	@RequestMapping(value = "/exam/shortAnswer", method = RequestMethod.PATCH)
	@ApiOperation(value = "提交简答题答案", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg updateStuAnswer(
			@ApiParam(required = true, name = "studentAnswer", value = "学生答案json数据") @RequestBody StudentAnswerParams studentAnswer,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			//判断试卷是否已经提交过
			Exam exam = examService.getExamByID(studentAnswer.getTest_id());
			if ( exam.getExam_id() == 1 ) {
				msg.errorCode = ErrorCode.EXAM_COMPLETENESS.code;
				msg.msg = ErrorCode.EXAM_COMPLETENESS.name;
			}
			shortAnswerService.updateExamStuAnswer(studentAnswer.getTest_id(),
					studentAnswer.getShortAnswerParams().getQuestion_id(),
					studentAnswer.getShortAnswerParams().getAnswer());
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
	 * 提交试卷
	 * 
	 * @return
	 */
	@RequestMapping(value = "/exam", method = RequestMethod.PATCH)
	@ApiOperation(value = "提交试卷，最终提交只包含简答题答案", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg updateExam(
			@ApiParam(required = true, name = "batchStudentAnswer", value = "学生答案json数据") @RequestBody BatchStudentAnswerParams BatchStudentAnswer,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			//判断试卷是否已经提交过
			Exam exam = examService.getExamByID(BatchStudentAnswer.getTest_id());
			if ( exam.getExam_id() == 1 ) {
				msg.errorCode = ErrorCode.EXAM_COMPLETENESS.code;
				msg.msg = ErrorCode.EXAM_COMPLETENESS.name;
			}
			List<QuestionParams> questionParamsList = BatchStudentAnswer.getQuestionParams();
			for (QuestionParams qusetionParams : questionParamsList) {
				shortAnswerService.updateExamStuAnswer(BatchStudentAnswer.getTest_id(),
						qusetionParams.getQuestion_id(),
						qusetionParams.getAnswer());
			}
			examService.updateStatus(BatchStudentAnswer.getTest_id());
			//TODO 单选，多选，判断题此处执行自动改卷
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
