package com.ncu.testbank.teacher.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.ShortAnswer;
import com.ncu.testbank.teacher.data.view.ShortAnswerView;

@Repository
public interface IShortAnswerDao {
	public int insertOne(ShortAnswer shortAnswer);

	public int updateOne(ShortAnswer shortAnswer);

	public int getCount(Map<String, Object> params);

	public List<ShortAnswerView> searchData(Map<String, Object> params);

	public ShortAnswer getOne(long question_id);

	public int deleteData(List<Long> question_id);
}
