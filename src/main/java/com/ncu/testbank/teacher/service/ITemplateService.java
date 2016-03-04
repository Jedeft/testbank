package com.ncu.testbank.teacher.service;

import java.util.List;

import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.data.Template;
import com.ncu.testbank.teacher.data.params.TemplateParams;

public interface ITemplateService {
	/**
	 * 添加考试模板，返回插入的考试模板信息
	 * @param templateParams
	 * @param user
	 * @return
	 */
	public Template insertOne(TemplateParams templateParams, User user);
	
	/**
	 * 修改考试模板
	 * @param templateParams
	 * @param user
	 */
	public void updateOne(TemplateParams templateParams);
	
	/**
	 * 获取考试模板信息
	 * @param template_id
	 * @return
	 */
	public Template getOne(long template_id);
	
	/**
	 * 根据课程ID和模板类型获取考试模板集合
	 * @param course_id
	 * @param type
	 * @return
	 */
	public List<Template> searchData(String course_id, Integer type);
	
	/**
	 * 根据模板ID删除模板
	 * @param template_id
	 */
	public void deleteData(List<Long> template_id);
}
