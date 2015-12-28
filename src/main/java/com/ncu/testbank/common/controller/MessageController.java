package com.ncu.testbank.common.controller;

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
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.base.response.ResponseQueryMsg;
import com.ncu.testbank.common.data.Message;
import com.ncu.testbank.common.data.params.MessageParams;
import com.ncu.testbank.common.data.view.MessageView;
import com.ncu.testbank.common.service.IMessageService;
import com.ncu.testbank.permission.data.User;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "message-api", description = "通知CRUD操作", position = 1)
@RestController
@RequestMapping("/common")
public class MessageController {

	@Autowired
	private IMessageService messageService;

	/**
	 * 分页获取通知主题信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/messages/title", method = RequestMethod.GET)
	@ApiOperation(value = "发送人检索主题", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseAdmin或者baseTeacher、baseStudent权限，请header中携带Token，检索自己发送的主题")
	public ResponseQueryMsg searchTitle(
			@ApiParam(required = true, name = "page", value = "分页数据") @RequestParam(value = "page", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "每页数据量") @RequestParam(value = "rows", required = true) Integer rows,
			@ApiIgnore HttpSession session) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			List<Message> academyList;
			PageInfo pageInfo = new PageInfo(page, rows);
			academyList = messageService.searchTitle(pageInfo, user);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = academyList;

			msg.total = pageInfo.getTotal();
			msg.totalPage = pageInfo.getTotalPage();
			msg.currentPage = pageInfo.getPage();
			msg.pageCount = academyList.size();
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
	 * 发送人分页获取通知信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/messages/sending", method = RequestMethod.GET)
	@ApiOperation(value = "发送人检索消息", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseAdmin或者baseTeacher、baseStudent权限，请header中携带Token，检索自己发送的消息")
	public ResponseQueryMsg searchSendMessage(
			@ApiParam(required = true, name = "page", value = "分页数据") @RequestParam(value = "page", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "每页数据量") @RequestParam(value = "rows", required = true) Integer rows,
			@ApiParam(required = false, name = "title_id", value = "主题ID") @RequestParam(value = "title_id", required = false) Long title_id,
			@ApiParam(required = false, name = "flag", value = "是否查看过（Y-查看，N-未查看）") @RequestParam(value = "flag", required = false) String flag,
			@ApiIgnore HttpSession session) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			List<MessageView> academyList;
			PageInfo pageInfo = new PageInfo(page, rows);
			Message message = new Message(title_id, user.getUsername(), null,
					flag);
			academyList = messageService.searchMessage(pageInfo, message);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = academyList;

			msg.total = pageInfo.getTotal();
			msg.totalPage = pageInfo.getTotalPage();
			msg.currentPage = pageInfo.getPage();
			msg.pageCount = academyList.size();
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
	 * 收件人分页获取通知信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/messages/receiving", method = RequestMethod.GET)
	@ApiOperation(value = "接收人检索消息", httpMethod = "GET", response = ResponseQueryMsg.class, notes = "需要baseAdmin或者baseTeacher、baseStudent权限，请header中携带Token，检索自己收到的消息")
	public ResponseQueryMsg searchReceiveMessage(
			@ApiParam(required = true, name = "page", value = "分页数据") @RequestParam(value = "page", required = true) Integer page,
			@ApiParam(required = true, name = "rows", value = "每页数据量") @RequestParam(value = "rows", required = true) Integer rows,
			@ApiParam(required = false, name = "flag", value = "是否查看过（Y-查看，N-未查看）") @RequestParam(value = "flag", required = false) String flag,
			@ApiIgnore HttpSession session) {
		ResponseQueryMsg msg = new ResponseQueryMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			List<MessageView> academyList;
			PageInfo pageInfo = new PageInfo(page, rows);
			Message message = new Message(null, null, user.getUsername(), flag);
			academyList = messageService.searchMessage(pageInfo, message);

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = academyList;

			msg.total = pageInfo.getTotal();
			msg.totalPage = pageInfo.getTotalPage();
			msg.currentPage = pageInfo.getPage();
			msg.pageCount = academyList.size();
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
	 * 发送消息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/messages", method = RequestMethod.POST)
	@ApiOperation(value = "发送消息", httpMethod = "POST", response = ResponseMsg.class, notes = "需要baseAdmin或者baseTeacher、baseStudent权限，请header中携带Token")
	public ResponseMsg sendMessage(
			@ApiParam(required = true, name = "message", value = "通知json数据") @RequestBody MessageParams messageParams,
			@ApiIgnore HttpSession session) {
		ResponseMsg msg = new ResponseMsg();
		try {
			User user = (User) session.getAttribute("currentUser");
			Message message = new Message();
			message.setMessage(messageParams.getMessage());
			message.setTitle(messageParams.getTitle());
			messageService.sendMessage(message, messageParams.getReceive_id(),
					user);

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
	 * 查看消息，将消息标记为已读
	 * 
	 * @return
	 */
	@RequestMapping(value = "/messages/{message_id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "查看消息，将消息标记为已读", httpMethod = "PATCH", response = ResponseMsg.class, notes = "需要baseAdmin或者baseTeacher、baseStudent权限，请header中携带Token")
	public ResponseMsg seeMessage(
			@ApiParam(required = true, name = "message_id", value = "通知ID") @PathVariable Long message_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			messageService.seeMessage(message_id);

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
	 * 根据主题ID删除消息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/messages", method = RequestMethod.DELETE)
	@ApiOperation(value = "根据主题ID删除消息", httpMethod = "DELETE", response = ResponseMsg.class, notes = "需要baseAdmin或者baseTeacher、baseStudent权限，请header中携带Token")
	public ResponseMsg deleteMessage(
			@ApiParam(required = true, name = "title_id", value = "主题ID数组,key为title_id") @RequestBody Map<String, List<Long>> map) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (map != null && !map.get("title_id").equals("")) {
				messageService.deleteMessage(map.get("title_id"));
			} else {
				msg.errorCode = 66666;
				msg.msg = "请选择删除的通知！";
				return msg;
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
	 * 根据主题ID或者消息ID获取一条消息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	@ApiOperation(value = "根据主题ID或者消息ID获取一条消息", httpMethod = "GET", response = ResponseMsg.class, notes = "需要baseAdmin或者baseTeacher、baseStudent权限，请header中携带Token")
	public ResponseMsg getMessage(
			@ApiParam(required = false, name = "title_id", value = "主题ID") @RequestParam(value = "title_id", required = false) Long title_id,
			@ApiParam(required = false, name = "message_id", value = "通知ID") @RequestParam(value = "message_id", required = false) Long message_id) {
		ResponseMsg msg = new ResponseMsg();
		try {
			if (title_id == null && message_id == null) {
				msg.errorCode = 66666;
				msg.msg = "请选择要查看的通知！";
				return msg;
			}
			MessageView messageView = null;
			if (message_id != null) {
				messageView = messageService.getMessageById(message_id);
			} else {
				messageView = messageService.getMessageByTitle(title_id);
			}

			msg.errorCode = ErrorCode.CALL_SUCCESS.code;
			msg.msg = ErrorCode.CALL_SUCCESS.name;
			msg.data = messageView;
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
