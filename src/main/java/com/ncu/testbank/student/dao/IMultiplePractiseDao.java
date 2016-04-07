package com.ncu.testbank.student.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.student.data.view.MultiplePractiseView;
import com.ncu.testbank.teacher.data.Question;

@Repository
public interface IMultiplePractiseDao {
	public int insertMultiple(Question question);

	public List<MultiplePractiseView> searchExamMultipleNoAnswer(Long exam_id);
	
	public List<MultiplePractiseView> searchExamMultiple(Long exam_id);
	
	public int deleteOne(Map<String, Object> params);
	
	public int updateStuAnswer(Map<String, Object> params);
}
