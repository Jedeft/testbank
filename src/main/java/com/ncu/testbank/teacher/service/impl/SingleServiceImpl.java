package com.ncu.testbank.teacher.service.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.utils.RandomID;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.dao.ISingleDao;
import com.ncu.testbank.teacher.data.Single;
import com.ncu.testbank.teacher.service.ISingleService;

@Service("singleService")
public class SingleServiceImpl implements ISingleService {

	@Autowired
	private ISingleDao singleDao;
	
	@Override
	public void insertWriting(Single single, User user) {
		single.setQuestion_id(RandomID.getID());
		single.setType(1);
		single.setCreate_time(new Timestamp(new Date().getTime()));
		single.setCreate_teacher_id(user.getUsername());
		
		if (singleDao.insertOne(single) < 1) {
			throw new ServiceException(new ErrorCode(30001, "单选题添加失败，请联系管理人员！"));
		}
	}

}
