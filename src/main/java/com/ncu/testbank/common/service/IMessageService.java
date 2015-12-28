package com.ncu.testbank.common.service;

import java.util.List;

import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.common.data.Message;
import com.ncu.testbank.common.data.view.MessageView;
import com.ncu.testbank.permission.data.User;

public interface IMessageService {

	/**
	 * 检索主题
	 * 
	 * @param page
	 * @param user
	 * @return
	 */
	public List<Message> searchTitle(PageInfo page, User user);

	/**
	 * 检索消息
	 * 
	 * @param page
	 * @param message : title_id, send_id, receive_id, flag
	 * @return
	 */
	public List<MessageView> searchMessage(PageInfo page, Message message);

	/**
	 * 发送消息
	 * 
	 * @param message
	 * @param receive_id
	 * @param user
	 */
	public void sendMessage(Message message, List<String> receive_id, User user);

	/**
	 * 查看消息
	 * 
	 * @param message_id
	 */
	public void seeMessage(long message_id);

	/**
	 * 根据主题ID删除消息
	 * 
	 * @param title_id
	 */
	public void deleteMessage(List<Long> title_id);

	/**
	 * 根据主题ID查找一条消息
	 * 
	 * @param title_id
	 * @return
	 */
	public MessageView getMessageByTitle(long title_id);

	/**
	 * 根据主键消息ID查找一条消息
	 * 
	 * @param message_id
	 * @return
	 */
	public MessageView getMessageById(long message_id);
}
