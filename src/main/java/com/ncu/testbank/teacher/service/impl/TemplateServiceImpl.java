package com.ncu.testbank.teacher.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.utils.RandomID;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.dao.ITemplateDao;
import com.ncu.testbank.teacher.data.Template;
import com.ncu.testbank.teacher.data.params.TemplateParams;
import com.ncu.testbank.teacher.service.ITemplateService;

@Service("templateService")
public class TemplateServiceImpl implements ITemplateService {

	@Autowired
	private ITemplateDao templateDao;

	@Override
	public Template insertOne(TemplateParams templateParams, User user) {
		Template template = templateParams.toTemplate();
		if (template.getEasy_ratio() + template.getHard_ratio()
				+ template.getMedium_ratio() != 1) {
			throw new ServiceException(new ErrorCode(30006,
					"考试模板中难度比例之和不为100%，请重新设置！"));
		}
		template.setTemplate_id(RandomID.getID());
		template.setUser_id(user.getUsername());
		template.setCreate_time(new Timestamp(new Date().getTime()));
		if (templateDao.insertOne(template) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "添加考试模板失败,请联系管理人员!"));
		}
		return template;
	}

	@Override
	public void updateOne(TemplateParams templateParams) {
		Template template = templateParams.toTemplate();
		template.setModify_time(new Timestamp(new Date().getTime()));
		if (templateDao.updateOne(template) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "修改考试模板失败,请联系管理人员!"));
		}
	}

	@Override
	public Template getOne(long template_id) {
		return templateDao.getOne(template_id);
	}

	@Override
	public List<Template> searchData(String course_id, Integer type) {
		Map<String, Object> params = new HashMap<>();
		params.put("course_id", course_id);
		params.put("type", type);
		return templateDao.searchData(params);
	}

	@Override
	public void deleteData(List<Long> template_id) {
		templateDao.deleteData(template_id);
	}

}
