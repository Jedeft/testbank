package com.ncu.testbank.teacher.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.ncu.testbank.base.exception.DaoException;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.exception.ShiroException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.base.response.ResponseQueryMsg;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.data.Multiple;
import com.ncu.testbank.teacher.data.params.DELQuestionParams;
import com.ncu.testbank.teacher.data.view.MultipleView;
import com.ncu.testbank.teacher.service.IMultipleService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "multiple-api", description = "多选题CRUD", position = 3)
@RestController
@RequestMapping("/teacher")
public class MultipleController {

	@Autowired
	private IMultipleService multipleService;

	/**
	 * 添加文字多选题
	 * @param multiple
	 * @param session
	 * @return
	 */
	@RequiresRoles("bankBuilder")
	@RequestMapping(value = "/multiples/writing", method = RequestMethod.POST)
	@ApiOperation(value = "添加文字多选题", httpMethod = "POST", response = ResponseMsg.class, notes = "需要bankBuilder权限，请header中携带Token")
	public ResponseMsg insertWriting(
			@ApiParam(required = true, name = "multiple", value = "添加的多选题json数据，ID为后台生成，多选答案逗号分隔") @RequestBody Multiple multiple,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			multipleService.insertWriting(multiple, user);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = multiple;
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
	 * 修改文字多选题
	 * @param multiple
	 * @param session
	 * @return
	 */
	@RequiresRoles("bankBuilder")
	@RequestMapping(value = "/multiples/writing", method = RequestMethod.PATCH)
	@ApiOperation(value = "修改文字多选题", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要bankBuilder权限，请header中携带Token")
	public ResponseMsg updateWriting(
			@ApiParam(required = true, name = "multiple", value = "修改的多选题json数据,多选答案逗号分隔") @RequestBody Multiple multiple,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			multipleService.updateWriting(multiple, user);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = multipleService.getMultiple(multiple.getQuestion_id());
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
	 * 分页获取multiple多选题信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/multiples", method = RequestMethod.GET)
	@ApiOperation(value = "检索多选题目", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseTeacher权限，请header中携带Token")
	public ResponseQueryMsg searchData(
			@ApiParam(required = true, name = "page", value = "分页数据") @RequestParam(value = "page", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "每页数据量") @RequestParam(value = "rows", required = true) Integer rows,
			@ApiParam(required = true, name = "point_id", value = "考点ID信息检索") @RequestParam(value = "point_id", required = true) Long point_id,
			@ApiParam(required = false, name = "type", value = "题目类型检索检索(1表示文字题目，2表示图片题目)") @RequestParam(value = "type", required = false) Integer type,
			@ApiParam(required = false, name = "level", value = "题目难度信息检索(1-5)") @RequestParam(value = "level", required = false) Integer level,
			@ApiParam(required = false, name = "question_id", value = "题目ID信息检索") @RequestParam(value = "question_id", required = false) Long question_id) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			List<MultipleView> multipleList;
			PageInfo pageInfo = new PageInfo(page, rows);
			Multiple multiple = new Multiple(question_id, point_id, type, level);
			multipleList = multipleService.searchData(pageInfo, multiple);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = multipleList;

			msg.total = pageInfo.getTotal();
			msg.totalPage = pageInfo.getTotalPage();
			msg.currentPage = pageInfo.getPage();
			msg.pageCount = multipleList.size();
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
	 * 获取题目
	 * 
	 * @return
	 */
	@RequestMapping(value = "/multiples/{question_id}", method = RequestMethod.GET)
	@ApiOperation(value = "获取题目", httpMethod = "GET", response = ResponseMsg.class, notes = "需要baseTeacher权限，请header中携带Token")
	public ResponseMsg getMultiple(
			@ApiParam(required = true, name = "question_id", value = "题目ID") @PathVariable long question_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			Multiple multiple = multipleService.getMultiple(question_id);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = multiple;
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
	 * 删除题目
	 * 
	 * @param question
	 * @return
	 */
	@RequiresRoles("bankBuilder")
	@RequestMapping(value = "/multiples", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除题目", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要bankBuilder权限，请header中携带Token")
	public ResponseMsg deleteMultiples(
			@ApiParam(required = true, name = "question", value = "删除题目json数组，集合中为question数组，每道题目携带题目类型type") @RequestBody Map<String, List<DELQuestionParams>> question) {
		ResponseMsg msg = new ResponseMsg();
		try {
			List<DELQuestionParams> params = null;
			if (question != null && question.get("question") != null) {
				params = question.get("question");
			} else {
				msg.errorCode = 66666;
				msg.msg = "请选择删除题目！";
				return msg;
			}
			multipleService.deleteQuestion(params);
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
	 * 插入图片题目
	 * 
	 * @param questionFile
	 * @param params
	 * @param session
	 * @return
	 */
	@RequiresRoles("bankBuilder")
	@RequestMapping(value = "/multiples/img", method = RequestMethod.POST)
	@ApiOperation(value = "插入图片题目", httpMethod = "POST", response = ResponseMsg.class, notes = "需要bankBuilder权限，请header中携带Token")
	public ResponseMsg insertImge(
			@ApiParam(required = true, name = "questionFile", value = "题目图片文件") @RequestParam(value = "questionFile", required = true) MultipartFile questionFile,
			@ApiParam(required = true, name = "point_id", value = "题目考点") @RequestParam Long point_id,
			@ApiParam(required = true, name = "level", value = "题目难度") @RequestParam Integer level,
			@ApiParam(required = true, name = "answer", value = "题目答案,多选答案逗号分隔") @RequestParam String answer,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			Multiple multiple = new Multiple(point_id, answer, 2, level);
			multipleService.insertImge(multiple, user, questionFile);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = multiple;
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
	 * 修改图片题目
	 * @param questionFile
	 * @param question_id
	 * @param point_id
	 * @param level
	 * @param answer
	 * @param session
	 * @return
	 */
	@RequiresRoles("bankBuilder")
	@RequestMapping(value = "/multiples/img", method = RequestMethod.PATCH)
	@ApiOperation(value = "修改图片题目", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要bankBuilder权限，请header中携带Token")
	public ResponseMsg updateImge(
			@ApiParam(required = false, name = "questionFile", value = "题目图片文件") @RequestParam(value = "questionFile", required = false) MultipartFile questionFile,
			@ApiParam(required = true, name = "question_id", value = "题目ID") @RequestParam(value = "question_id", required = false) Long question_id,
			@ApiParam(required = false, name = "point_id", value = "题目考点ID") @RequestParam(value = "point_id", required = false) Long point_id,
			@ApiParam(required = false, name = "level", value = "题目难度") @RequestParam(value = "level", required = false) Integer level,
			@ApiParam(required = false, name = "answer", value = "题目答案,多选答案逗号分隔") @RequestParam(value = "answer", required = false) String answer,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");

			Multiple multiple = new Multiple();
			if (answer != null) {
				multiple.setAnswer(answer);
			}
			if (question_id != null) {
				multiple.setQuestion_id(question_id);
			}
			if (level != null) {
				multiple.setLevel(level);
			}
			if (point_id != null) {
				multiple.setPoint_id(point_id);
			}
			multipleService.updateImge(multiple, user, questionFile);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = multiple;
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
