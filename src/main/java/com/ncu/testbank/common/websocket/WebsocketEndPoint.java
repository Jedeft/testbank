package com.ncu.testbank.common.websocket;

import javax.annotation.Resource;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.ncu.testbank.base.utils.JSONUtils;
import com.ncu.testbank.base.utils.RandomID;
import com.ncu.testbank.permission.data.User;

public class WebsocketEndPoint extends TextWebSocketHandler {

	
	@Override
	protected void handleTextMessage(WebSocketSession session,
			TextMessage message) throws Exception {
		super.handleTextMessage(session, message);
//		TextMessage returnMessage = new TextMessage(message.getPayload()
//				+ " received at server");
//		session.sendMessage(returnMessage);
		while(true) {
			Thread.sleep(2000);
			User user = new User();
			user.setName(RandomID.getID()+"");
			TextMessage retuMessage = new TextMessage(JSONUtils.convertObject2Json(user));
			session.sendMessage(retuMessage);
		}
	}
}