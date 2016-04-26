package com.ncu.testbank.teacher.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.Question;
import com.ncu.testbank.teacher.data.Single;
import com.ncu.testbank.teacher.data.view.SingleView;

@Repository
public interface ISingleDao {
	public int insertOne(Single single);

	public int updateOne(Single single);

	public int getCount(Map<String, Object> params);

	public List<SingleView> searchData(Map<String, Object> params);

	public Single getOne(long question_id);

	public int deleteData(List<Long> question_id);

	public List<Question> searchByCourse(String course_id);
}
