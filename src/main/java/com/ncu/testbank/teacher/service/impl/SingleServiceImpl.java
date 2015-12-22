package com.ncu.testbank.teacher.service.impl;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.BeanToMapUtils;
import com.ncu.testbank.base.utils.RandomID;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.dao.ISingleDao;
import com.ncu.testbank.teacher.data.Single;
import com.ncu.testbank.teacher.data.view.SingleView;
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

	@Override
	public void updateWriting(Single single, User user) {
		single.setModify_time(new Timestamp(new Date().getTime()));
		single.setModify_teacher_id(user.getUsername());

		if (singleDao.updateOne(single) < 1) {
			throw new ServiceException(new ErrorCode(30001, "单选题修改失败，请联系管理人员！"));
		}
	}

	@Override
	public List<SingleView> searchData(PageInfo page, Single single)
			throws IllegalAccessException, InvocationTargetException,
			IntrospectionException {
		Map<String, Object> params = BeanToMapUtils.convertBean(single);
		int count = singleDao.getCount(params);
		page.setTotal(count);
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		}
		page.setTotalPage(count / page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的题目！"));
		}
		// 数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return singleDao.searchData(params);
	}

}
