package com.ncu.testbank.admin.service.impl;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncu.testbank.admin.dao.IBankBuilderDao;
import com.ncu.testbank.admin.data.BankBuilder;
import com.ncu.testbank.admin.data.Teacher;
import com.ncu.testbank.admin.data.view.BankBuilderView;
import com.ncu.testbank.admin.service.IBankBuilderService;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.BeanToMapUtils;
import com.ncu.testbank.permission.dao.IRoleDao;

@Service("bankBuilderService")
public class BankBuilderServiceImpl implements IBankBuilderService {

	@Autowired
	private IBankBuilderDao bankBuilderDao;
	@Autowired
	private IRoleDao roleDao;

	@Override
	public List<BankBuilderView> searchData(PageInfo page, BankBuilderView bankBuilder)
			throws IllegalAccessException, InvocationTargetException,
			IntrospectionException {
		Map<String, Object> params = BeanToMapUtils.convertBean(bankBuilder);
		int count = bankBuilderDao.getCount(params);
		page.setTotal(count);
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		}
		page.setTotalPage(count / page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的题库建设人员！"));
		}
		// 数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return bankBuilderDao.searchData(params);
	}

	@Override
	public void insertOne(BankBuilder bankBuilder) {
		if (bankBuilderDao.insertOne(bankBuilder) < 1) {
			throw new ServiceException(new ErrorCode(30001,
					"添加题库建设人员失败，请联系管理人员！"));
		}
		Map<String, Object> params = new HashMap<>();
		params.put("username", bankBuilder.getTeacher_id());
		// 题库角色建设者ID 为5
		params.put("role_id", 5);
		// 角色等级为1
		params.put("level", 0);
		if (roleDao.putRole(params) < 1) {
			throw new ServiceException(new ErrorCode(80003,
					"添加题库建设权限失败，请联系管理人员！"));
		}
	}

	@Override
	public void deleteOne(BankBuilder bankBuilder) {
		if (bankBuilderDao.deleteOne(bankBuilder) < 1) {
			throw new ServiceException(new ErrorCode(30001,
					"删除题库建设人员失败，请联系管理人员！"));
		}
		Map<String, Object> params = new HashMap<>();
		params.put("username", bankBuilder.getTeacher_id());
		// 题库角色建设者ID 为5
		params.put("role_id", 5);
		if (roleDao.deleteRole(params) < 1) {
			throw new ServiceException(new ErrorCode(80003,
					"删除题库建设权限失败，请联系管理人员！"));
		}
	}

}
