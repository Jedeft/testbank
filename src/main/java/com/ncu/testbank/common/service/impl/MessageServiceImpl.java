package com.ncu.testbank.common.service.impl;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.BeanToMapUtils;
import com.ncu.testbank.base.utils.RandomID;
import com.ncu.testbank.common.dao.IMessageDao;
import com.ncu.testbank.common.data.Message;
import com.ncu.testbank.common.data.view.MessageView;
import com.ncu.testbank.common.service.IMessageService;
import com.ncu.testbank.permission.dao.IUserDao;
import com.ncu.testbank.permission.data.User;

@Service("messageService")
public class MessageServiceImpl implements IMessageService {

	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private IMessageDao messageDao;

	@Autowired
	private IUserDao userDao;

	@Override
	public List<Message> searchTitle(PageInfo page, User user) {
		Map<String, Object> params = new HashMap<>();
		int count = messageDao.getTitleCount(user.getUsername());
		page.setTotal(count);
		params.put("send_id", user.getUsername());
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		}
		page.setTotalPage(count / page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的通知！"));
		}
		// 数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return messageDao.searchTitleData(params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MessageView> searchMessage(PageInfo page, Message message) {
		Map<String, Object> params = null;
		try {
			params = BeanToMapUtils.convertBean(message);
		} catch (IllegalAccessException | InvocationTargetException
				| IntrospectionException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.MAP_CONVERT_ERROR);
		}
		int count = messageDao.getMessageCount(params);
		page.setTotal(count);
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		}
		page.setTotalPage(count / page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的通知！"));
		}
		// 数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return messageDao.searchMessageData(params);
	}

	@Override
	public void sendMessage(Message message, List<String> list, User user) {
		Timestamp now = new Timestamp(new Date().getTime());
		long title_id = RandomID.getID();
		List<String> failList = new ArrayList<>();
		for (String receive_id : list) {
			message.setSend_id(user.getUsername());
			message.setReceive_id(receive_id);
			message.setSend_time(now);
			message.setTitle_id(title_id);
			message.setMessage_id(RandomID.getID());
			message.setFlag("N");

			if (messageDao.insertOne(message) < 1) {
				failList.add(receive_id);
			}
		}
		// 通知失败的用户
		if (failList != null && failList.size() > 1) {
			StringBuilder sb = new StringBuilder().append("以下用户通知失败，请联系管理人员：");
			List<User> users = userDao.getUsers(failList);
			for (int i = 0; i < users.size(); i++) {
				if (i != users.size() - 1) {
					sb.append(users.get(i).getName() + ",");
				} else {
					sb.append(users.get(i).getName());
				}
			}
			throw new ServiceException(new ErrorCode(30006, sb.toString()));
		}
	}

	@Override
	public void seeMessage(long message_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("message_id", message_id);
		params.put("receive_time", new Timestamp(new Date().getTime()));
		params.put("flag", "Y");
		if (messageDao.updateOne(params) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "更新消息状态失败，请联系管理人员！"));
		}
	}

	@Override
	public void deleteMessage(List<Long> title_id) {
		messageDao.deleteMessage(title_id);
	}

	@Override
	public MessageView getMessageByTitle(long title_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("title_id", title_id);
		return messageDao.getMessage(params);
	}

	@Override
	public MessageView getMessageById(long message_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("message_id", message_id);
		return messageDao.getMessage(params);
	}

}
