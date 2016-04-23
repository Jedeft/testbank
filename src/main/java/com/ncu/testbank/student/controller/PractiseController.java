package com.ncu.testbank.student.controller;

import java.sql.Timestamp;
import java.util.Date;

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
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.student.data.Practise;
import com.ncu.testbank.student.data.params.PractiseParams;
import com.ncu.testbank.student.data.params.StudentAnswerParams;
import com.ncu.testbank.student.data.view.PractisePaperView;
import com.ncu.testbank.student.service.IPractiseService;
import com.ncu.testbank.teacher.data.Template;
import com.ncu.testbank.teacher.service.IJudgeService;
import com.ncu.testbank.teacher.service.IMultipleService;
import com.ncu.testbank.teacher.service.ISingleService;
import com.ncu.testbank.teacher.service.ITemplateService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "practise-api", description = "练习模块", position = 4)
@RestController
@RequestMapping("/student")
public class PractiseController {
	@Autowired
	private ITemplateService templateService;

	@Autowired
	private IPractiseService practiseService;

	@Autowired
	private ISingleService singleService;

	@Autowired
	private IMultipleService multipleService;

	@Autowired
	private IJudgeService judgeService;

	/**
	 * 根据模板生成练习试卷
	 * 
	 * @param practiseParams
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/practise", method = RequestMethod.POST)
	@ApiOperation(value = "根据模板生成练习试卷", httpMethod = "POST", response = ResponseMsg.class, notes = "需要teacher权限，请header中携带Token")
	public ResponseMsg insertWriting(
			@ApiParam(required = true, name = "examParams", value = "练习参数json数据") @RequestBody PractiseParams practiseParams,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (practiseParams.getHour() == null) {
				practiseParams.setHour(0);
			}
			if (practiseParams.getMinute() == null) {
				practiseParams.setMinute(0);
			}
			User user = (User) session.getAttribute("currentUser");
			Template template = templateService.getOne(practiseParams
					.getTemplate_id());
			Date now = new Date();
			Timestamp start = new Timestamp(now.getTime());
			Timestamp end = new Timestamp(now.getTime()
					+ practiseParams.getHour() * 60 * 3600 * 1000
					+ practiseParams.getMinute() * 3600 * 1000);
			Practise practise = practiseService.createPractise(template,
					user.getUsername(), start, end);
			PractisePaperView ppv = practiseService
					.getPracitseDetailByIDNoAnswer(practise.getPractise_id());
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = ppv;
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
	@RequestMapping(value = "/practise/single", method = RequestMethod.PATCH)
	@ApiOperation(value = "提交单选题答案", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg updateSingleStuAnswer(
			@ApiParam(required = true, name = "studentAnswer", value = "学生答案json数据") @RequestBody StudentAnswerParams studentAnswer,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			// 判断练习是否已经提交过
			Practise practise = practiseService.getPractiseByID(studentAnswer
					.getTest_id());
			if (practise.getStatus() == 1) {
				msg.errorCode = ErrorCode.EXAM_COMPLETENESS.code;
				msg.msg = ErrorCode.EXAM_COMPLETENESS.name;
			}
			singleService.updatePractiseStuAnswer(studentAnswer.getTest_id(),
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
	@RequestMapping(value = "/practise/multiple", method = RequestMethod.PATCH)
	@ApiOperation(value = "提交多选题答案，多选答案用逗号分隔", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg updateMultipleStuAnswer(
			@ApiParam(required = true, name = "studentAnswer", value = "学生答案json数据") @RequestBody StudentAnswerParams studentAnswer,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			// 判断练习是否已经提交过
			Practise practise = practiseService.getPractiseByID(studentAnswer
					.getTest_id());
			if (practise.getPractise_id() == 1) {
				msg.errorCode = ErrorCode.EXAM_COMPLETENESS.code;
				msg.msg = ErrorCode.EXAM_COMPLETENESS.name;
			}
			multipleService.updatePractiseStuAnswer(studentAnswer.getTest_id(),
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
	@RequestMapping(value = "/practise/judge", method = RequestMethod.PATCH)
	@ApiOperation(value = "提交判断题答案", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg updateJudgeStuAnswer(
			@ApiParam(required = true, name = "studentAnswer", value = "学生答案json数据") @RequestBody StudentAnswerParams studentAnswer,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			// 判断练习是否已经提交过
			Practise practise = practiseService.getPractiseByID(studentAnswer
					.getTest_id());
			if (practise.getPractise_id() == 1) {
				msg.errorCode = ErrorCode.EXAM_COMPLETENESS.code;
				msg.msg = ErrorCode.EXAM_COMPLETENESS.name;
			}
			judgeService.updatePractiseStuAnswer(studentAnswer.getTest_id(),
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
	 * 提交练习
	 * 
	 * @return
	 */
	@RequestMapping(value = "/practise", method = RequestMethod.PATCH)
	@ApiOperation(value = "提交练习", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要baseStudent权限，请header中携带Token")
	public ResponseMsg updateExam(
			@ApiParam(required = true, name = "practise_id", value = "学生练习ID") @RequestParam(value = "practise_id", required = true) Long practise_id,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			// 判断练习是否已经提交过
			Practise practise = practiseService.getPractiseByID(practise_id);
			if (practise.getPractise_id() == 1) {
				msg.errorCode = ErrorCode.EXAM_COMPLETENESS.code;
				msg.msg = ErrorCode.EXAM_COMPLETENESS.name;
			}
			practiseService.updateStatus(practise_id);
			// TODO 单选，多选，判断题此处执行自动改卷
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

	// /**
	// * 分页获取在线考试学生考试情况信息
	// *
	// * @return
	// */
	// @RequestMapping(value = "/exam/online", method = RequestMethod.GET)
	// @ApiOperation(value = "检索在线考试学生考试情况信息", httpMethod = "GET", response =
	// ResponseQueryMsg.class, notes = "需要baseTeacher权限，请header中携带Token")
	// public ResponseQueryMsg searchOnlineExam(
	// @ApiParam(required = true, name = "page", value = "分页数据")
	// @RequestParam(value = "page", required = true) Integer page,
	// @ApiParam(required = true, name = "rows", value = "每页数据量")
	// @RequestParam(value = "rows", required = true) Integer rows,
	// @ApiParam(required = true, name = "course_id", value = "课程ID信息检索")
	// @RequestParam(value = "course_id", required = true) String course_id,
	// @ApiIgnore HttpSession session) {
	// ResponseQueryMsg msg = new ResponseQueryMsg();
	// try {
	// PageInfo pageInfo = new PageInfo(page, rows);
	// User user = (User) session.getAttribute("currentUser");
	// List<OnlineExamView> examList = examService.searchOnlineByTID(
	// user.getUsername(), course_id);
	//
	// msg.errorCode = ErrorCode.CALL_SUCCESS.code;
	// msg.msg = ErrorCode.CALL_SUCCESS.name;
	// msg.data = examList;
	//
	// msg.total = pageInfo.getTotal();
	// msg.totalPage = pageInfo.getTotalPage();
	// msg.currentPage = pageInfo.getPage();
	// msg.pageCount = examList != null ? examList.size() : 0;
	// } catch (ShiroException e) {
	// ErrorCode error = e.getErrorCode();
	// msg.errorCode = error.code;
	// msg.msg = error.name;
	// } catch (ServiceException e) {
	// ErrorCode error = e.getErrorCode();
	// msg.errorCode = error.code;
	// msg.msg = error.name;
	// } catch (DaoException e) {
	// ErrorCode error = e.getErrorCode();
	// msg.errorCode = error.code;
	// msg.msg = error.name;
	// }
	// return msg;
	// }
	//
	// /**
	// * 分页获取历史试卷信息(笔试)
	// *
	// * @return
	// */
	// @RequestMapping(value = "/exam/history", method = RequestMethod.GET)
	// @ApiOperation(value = "检索历史试卷信息（笔试）", httpMethod = "GET", response =
	// ResponseQueryMsg.class, notes = "需要baseTeacher权限，请header中携带Token")
	// public ResponseQueryMsg searchHistoryExam(
	// @ApiParam(required = true, name = "page", value = "分页数据")
	// @RequestParam(value = "page", required = true) Integer page,
	// @ApiParam(required = true, name = "rows", value = "每页数据量")
	// @RequestParam(value = "rows", required = true) Integer rows,
	// @ApiParam(required = true, name = "course_id", value = "课程ID信息检索")
	// @RequestParam(value = "course_id", required = true) String course_id,
	// @ApiIgnore HttpSession session) {
	// ResponseQueryMsg msg = new ResponseQueryMsg();
	// try {
	// PageInfo pageInfo = new PageInfo(page, rows);
	// User user = (User) session.getAttribute("currentUser");
	// List<HistoryExamView> examList = examService.searchHistoryByTID(
	// user.getUsername(), course_id);
	//
	// msg.errorCode = ErrorCode.CALL_SUCCESS.code;
	// msg.msg = ErrorCode.CALL_SUCCESS.name;
	// msg.data = examList;
	//
	// msg.total = pageInfo.getTotal();
	// msg.totalPage = pageInfo.getTotalPage();
	// msg.currentPage = pageInfo.getPage();
	// msg.pageCount = examList != null ? examList.size() : 0;
	// } catch (ShiroException e) {
	// ErrorCode error = e.getErrorCode();
	// msg.errorCode = error.code;
	// msg.msg = error.name;
	// } catch (ServiceException e) {
	// ErrorCode error = e.getErrorCode();
	// msg.errorCode = error.code;
	// msg.msg = error.name;
	// } catch (DaoException e) {
	// ErrorCode error = e.getErrorCode();
	// msg.errorCode = error.code;
	// msg.msg = error.name;
	// }
	// return msg;
	// }
	//
	// /**
	// * 分页获取历史试卷信息(笔试)
	// *
	// * @return
	// */
	// @RequestMapping(value = "/exam/detail", method = RequestMethod.GET)
	// @ApiOperation(value = "查看考试试卷详细信息（包含题目），笔试类型包含正确答案，在线考试类型包含学生答案和正确答案",
	// httpMethod = "GET", response = ResponseQueryMsg.class, notes =
	// "需要baseTeacher权限，请header中携带Token")
	// public ResponseMsg getExamDetail(
	// @ApiParam(required = true, name = "exam_id", value = "试卷ID")
	// @RequestParam(value = "exam_id", required = true) Long exam_id) {
	// ResponseMsg msg = new ResponseMsg();
	// try {
	// ExamPaperView examPaperView = examService.getExamByID(exam_id);
	// msg.errorCode = ErrorCode.CALL_SUCCESS.code;
	// msg.msg = ErrorCode.CALL_SUCCESS.name;
	// msg.data = examPaperView;
	// } catch (ShiroException e) {
	// ErrorCode error = e.getErrorCode();
	// msg.errorCode = error.code;
	// msg.msg = error.name;
	// } catch (ServiceException e) {
	// ErrorCode error = e.getErrorCode();
	// msg.errorCode = error.code;
	// msg.msg = error.name;
	// } catch (DaoException e) {
	// ErrorCode error = e.getErrorCode();
	// msg.errorCode = error.code;
	// msg.msg = error.name;
	// }
	// return msg;
	// }
}
