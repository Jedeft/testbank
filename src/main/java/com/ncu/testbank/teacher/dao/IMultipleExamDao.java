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

	/**
	 * 更新题目获得分数
	 * 
	 * @param exam_id
	 * @param question_id
	 * @param score
	 * @return
	 */
	public int updateScore(Map<String, Object> params);
}
