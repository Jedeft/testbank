package com.ncu.testbank.common.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WsMessageController {
	
	//Spring实现的发送模板类。发送值通过jackson转换为json类型
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	//客户端发送消息，请求为/websocket/broadcast则会跳转到该路由中
	@MessageMapping("/broadcast")
	public void chat(@RequestBody Map<String, Object> map) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("message", map.get("message"));
		result.put("code", new Date().getTime());
		simpMessagingTemplate.convertAndSend("/client/messages/Jerry", result);
	}
	
}
