package com.ncu.testbank.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.common.data.Message;
import com.ncu.testbank.common.data.view.MessageView;

@Repository
public interface IMessageDao {
	/**
	 * 获取message数量
	 * 
	 * @param params
	 *            : title_id, send_id, receive_id, flag
	 * @return
	 */
	public int getMessageCount(Map<String, Object> params);

	/**
	 * 获取主题数量
	 * 
	 * @param send_id
	 * @return
	 */
	public int getTitleCount(String send_id);

	/**
	 * 检索消息
	 * 
	 * @param params
	 *            : title_id, send_id, receive_id, flag, page, rows
	 * @return
	 */
	public List<MessageView> searchMessageData(Map<String, Object> params);

	/**
	 * 检索主题
	 * 
	 * @param params
	 *            : send_id, page, rows
	 * @return
	 */
	public List<Message> searchTitleData(Map<String, Object> params);

	/**
	 * 根据消息ID或者主题ID来查找Message
	 * 
	 * @param params
	 *            ： message_id, title_id
	 * @return
	 */
	public MessageView getMessage(Map<String, Object> params);

	/**
	 * 添加消息
	 * 
	 * @param message
	 * @return
	 */
	public int insertOne(Message message);

	/**
	 * 通过主题ID删除组消息
	 * 
	 * @param title_id
	 * @return
	 */
	public int deleteMessage(List<Long> title_id);

	/**
	 * 修改只可添加接收时间以及接收标志
	 * 
	 * @param params
	 *            : receive_time, flag, message_id
	 * @return
	 */
	public int updateOne(Map<String, Object> params);
}
