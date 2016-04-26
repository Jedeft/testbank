package com.ncu.testbank.teacher.dao;

import java.util.List;
import java.util.Map;

import com.ncu.testbank.teacher.data.Multiple;
import com.ncu.testbank.teacher.data.Question;
import com.ncu.testbank.teacher.data.view.MultipleView;

public interface IMultipleDao {
	public int insertOne(Multiple single);

	public int updateOne(Multiple single);

	public int getCount(Map<String, Object> params);

	public List<MultipleView> searchData(Map<String, Object> params);

	public Multiple getOne(long question_id);

	public int deleteData(List<Long> question_id);

	public List<Question> searchByCourse(String course_id);
}
