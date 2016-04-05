package com.ncu.testbank.teacher.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.Question;
import com.ncu.testbank.teacher.data.view.MultipleExamView;

@Repository
public interface IMultipleExamDao {
	public int insertMultiple(Question question);

	public List<MultipleExamView> searchExamMultipleNoAnswer(Long exam_id);
	
	public List<MultipleExamView> searchExamMultiple(Long exam_id);
	
	public int deleteOne(Map<String, Object> params);
	
	public int updateStuAnswer(Map<String, Object> params);
}
