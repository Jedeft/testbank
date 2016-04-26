package com.ncu.testbank.student.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.student.data.view.MultiplePractiseView;
import com.ncu.testbank.teacher.data.Question;

@Repository
public interface IMultiplePractiseDao {
	public int insertMultiple(Question question);

	public List<MultiplePractiseView> searchPractiseMultipleNoAnswer(Long practise_id);
	
	public List<MultiplePractiseView> searchPractiseMultiple(Long practise_id);
	
	public int deleteOne(Map<String, Object> params);
	
	public int updateStuAnswer(Map<String, Object> params);
	
	/**
	 * 更新题目正确性
	 * @param practise_id
	 * @param question_id
	 * @param status
	 * @return
	 */
	public int updateStatus(Long practise_id, Long question_id, String status);
}
