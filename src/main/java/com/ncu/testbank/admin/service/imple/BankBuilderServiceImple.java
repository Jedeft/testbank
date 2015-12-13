package com.ncu.testbank.admin.service.imple;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncu.testbank.admin.dao.IBankBuilderDao;
import com.ncu.testbank.admin.data.Teacher;
import com.ncu.testbank.admin.service.IBankBuilderService;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.BeanToMapUtils;

@Service("bankBuilderService")
public class BankBuilderServiceImple implements IBankBuilderService {

	@Autowired
	private IBankBuilderDao bankBuilderDao;

	@Override
	public List<Teacher> searchData(PageInfo page, Teacher teacher)
			throws IllegalAccessException, InvocationTargetException,
			IntrospectionException {
		Map<String, Object> params = BeanToMapUtils.convertBean(teacher);
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
	public void insertOne(String teacher_id) {
		if (bankBuilderDao.insertOne(teacher_id) < 1) {
			throw new ServiceException(new ErrorCode(30001,
					"添加题库建设人员失败，请联系管理人员！"));
		}
	}

	@Override
	public void deleteOne(String teacher_id) {
		if (bankBuilderDao.deleteOne(teacher_id) < 1) {
			throw new ServiceException(new ErrorCode(30001,
					"删除题库建设人员失败，请联系管理人员！"));
		}
	}

	@Override
	public void deleteData(List<String> teacher_id) {
		bankBuilderDao.deleteData(teacher_id);
	}

	@Override
	public Teacher getBankBuilder(String teacher_id) {
		return bankBuilderDao.getBankBuilder(teacher_id);
	}

}
