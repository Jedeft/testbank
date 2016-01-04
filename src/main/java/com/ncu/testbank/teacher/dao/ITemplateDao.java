package com.ncu.testbank.teacher.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.Template;

@Repository
public interface ITemplateDao {
	public List<Template> searchData(String course_id);

	public int insertOne(Template template);

	public int updateOne(Template template);

	public Template getOne(long template_id);

	public int deleteData(List<Long> template_id);
}