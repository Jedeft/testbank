package com.ncu.testbank.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

//在spring-mvc.xml中配置了<mvc:annotation-driven />后，此处不用再写此注解。会报错
//@EnableWebMvc
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		//客户端(浏览器)接收推送消息的url地址开头部分
		config.enableSimpleBroker("/client/messages");
		
		//服务器辨别是否是一个websocket请求。凡http://ip:port/projectName/websocket/*类的url，都为websocket请求
		config.setApplicationDestinationPrefixes("/websocket");
	}

	//客户端建立websocket连接的url请求地址
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket/messages", "/client/sendResult").withSockJS();
	}
	
}