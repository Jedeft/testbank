package com.ncu.testbank.common.controller.websocket;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.ResponseMsg;
import com.ncu.testbank.common.data.Message;
import com.ncu.testbank.common.data.params.MessageParams;
import com.ncu.testbank.common.service.IMessageService;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.permission.service.IUserService;

@RestController
public class WsMessageController {

	@Autowired
	private IMessageService messageService;

	@Autowired
	private IUserService userService;

	// Spring实现的发送模板类。发送值通过jackson转换为json类型
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	// 客户端发送消息，请求为/websocket/broadcast则会跳转到该路由中
	// Principal获取当前用户本地缓存的用户信息.
	// 客户端接收url地址为/client/messages/{username}
	// 发送通知完成后的消息通知给地址/client/sendResult
	@MessageMapping("/broadcast")
	public void broadcast(@RequestBody MessageParams messageParams,
			Principal principal) {
		String username = principal.getName();

		User user = userService.getUser(username);
		Map<String, Object> response = new HashMap<>();
		response.put("send_id", username);
		response.put("send_name", user.getName());
		response.put("message", messageParams.getMessage());
		response.put("title", messageParams.getTitle());

		Message message = new Message();
		message.setMessage(messageParams.getMessage());
		message.setTitle(messageParams.getTitle());
		ResponseMsg msg = new ResponseMsg();
		try {
			messageService.sendMessage(message, messageParams.getReceive_id(),
					user);
			msg.errorCode = 0;
			msg.msg = "通知发送成功！";
		} catch (ServiceException e) {
			ErrorCode error = e.getErrorCode();
			msg.errorCode = error.code;
			msg.msg = error.name;
		}
		// 发送时间是由messageService中统一确定的。
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		response.put("send_time", sdf.format(message.getSend_time()));
		for (String receive_id : messageParams.getReceive_id()) {
			simpMessagingTemplate.convertAndSend("/client/messages/"
					+ receive_id, response);
		}

		// 发送通知完成后的结果通知给地址
		simpMessagingTemplate.convertAndSend("/client/sendResult", msg);
	}

	
}
