package com.ncu.testbank.teacher.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.Question;
import com.ncu.testbank.teacher.data.view.SingleExamView;

@Repository
public interface ISingleExamDao {
	public int insertSingle(Question question);

	public List<SingleExamView> searchExamSingleNoAnswer(Long exam_id);
	
	public int deleteOne(Map<String, Object> params);
}
