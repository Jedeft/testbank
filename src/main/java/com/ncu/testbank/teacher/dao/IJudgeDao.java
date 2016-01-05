package com.ncu.testbank.teacher.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.Judge;
import com.ncu.testbank.teacher.data.Question;
import com.ncu.testbank.teacher.data.view.JudgeView;

@Repository
public interface IJudgeDao {
	public int insertOne(Judge judge);

	public int updateOne(Judge judge);

	public int getCount(Map<String, Object> params);

	public List<JudgeView> searchData(Map<String, Object> params);

	public Judge getOne(long question_id);

	public int deleteData(List<Long> question_id);
	
	public List<Question> searchByCourse(String course_id);
}
