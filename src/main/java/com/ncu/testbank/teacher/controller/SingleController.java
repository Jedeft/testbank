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
import com.ncu.testbank.teacher.data.Single;
import com.ncu.testbank.teacher.data.params.DELQuestionParams;
import com.ncu.testbank.teacher.data.view.SingleView;
import com.ncu.testbank.teacher.service.ISingleService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "single-api", description = "单选题CRUD", position = 3)
@RestController
@RequestMapping("/teacher")
public class SingleController {

	@Autowired
	private ISingleService singleService;

	/**
	 * 添加文字单选题
	 * 
	 * @param single
	 * @param session
	 * @return
	 */
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
			msg.data = single;
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
	 * 修改文字单选题
	 * 
	 * @param single
	 * @param session
	 * @return
	 */
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
			msg.data = singleService.getSingle(single.getQuestion_id());
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
			msg.pageCount = singleList != null ? singleList.size() : 0;
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
	@RequestMapping(value = "/singles/{question_id}", method = RequestMethod.GET)
	@ApiOperation(value = "获取题目", httpMethod = "GET", response = ResponseMsg.class, notes = "需要baseTeacher权限，请header中携带Token")
	public ResponseMsg getSingle(
			@ApiParam(required = true, name = "question_id", value = "题目ID") @PathVariable long question_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			Single single = singleService.getSingle(question_id);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = single;
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
	@RequestMapping(value = "/singles", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除题目", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要bankBuilder权限，请header中携带Token")
	public ResponseMsg deleteSingles(
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
			singleService.deleteQuestion(params);
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
	// @RequiresRoles("bankBuilder")
	@RequestMapping(value = "/singles/img", method = RequestMethod.POST)
	@ApiOperation(value = "插入图片题目", httpMethod = "POST", response = ResponseMsg.class, notes = "需要bankBuilder权限，请header中携带Token")
	public ResponseMsg insertImge(
			@ApiParam(required = true, name = "questionFile", value = "题目图片文件") @RequestParam(value = "questionFile", required = true) MultipartFile questionFile,
			@ApiParam(required = true, name = "point_id", value = "题目考点") @RequestParam Long point_id,
			@ApiParam(required = true, name = "level", value = "题目难度") @RequestParam Integer level,
			@ApiParam(required = true, name = "answer", value = "题目答案") @RequestParam String answer,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			Single single = new Single(point_id, answer, 2, level);
			singleService.insertImge(single, user, questionFile);
			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = single;
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
	 * 
	 * @param questionFile
	 * @param question_id
	 * @param point_id
	 * @param level
	 * @param answer
	 * @param session
	 * @return
	 */
	@RequiresRoles("bankBuilder")
	@RequestMapping(value = "/singles/img", method = RequestMethod.PATCH)
	@ApiOperation(value = "修改图片题目", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要bankBuilder权限，请header中携带Token")
	public ResponseMsg updateImge(
			@ApiParam(required = false, name = "questionFile", value = "题目图片文件") @RequestParam(value = "questionFile", required = false) MultipartFile questionFile,
			@ApiParam(required = true, name = "question_id", value = "题目ID") @RequestParam(value = "question_id", required = false) Long question_id,
			@ApiParam(required = false, name = "point_id", value = "题目考点ID") @RequestParam(value = "point_id", required = false) Long point_id,
			@ApiParam(required = false, name = "level", value = "题目难度") @RequestParam(value = "level", required = false) Integer level,
			@ApiParam(required = false, name = "answer", value = "题目答案") @RequestParam(value = "answer", required = false) String answer,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			Single single = new Single();
			if (answer != null) {
				single.setAnswer(answer);
			}
			if (question_id != null) {
				single.setQuestion_id(question_id);
			}
			if (level != null) {
				single.setLevel(level);
			}
			if (point_id != null) {
				single.setPoint_id(point_id);
			}
			singleService.updateImge(single, user, questionFile);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = single;
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
