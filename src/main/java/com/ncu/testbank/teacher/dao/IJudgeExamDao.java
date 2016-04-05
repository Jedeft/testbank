package com.ncu.testbank.teacher.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.Question;
import com.ncu.testbank.teacher.data.view.JudgeExamView;

@Repository
public interface IJudgeExamDao {
	public int insertJudge(Question question);

	public List<JudgeExamView> searchExamJudgeNoAnswer(Long exam_id);
	
	public List<JudgeExamView> searchExamJudge(Long exam_id);
	
	public int deleteOne(Map<String, Object> params);
	
	public int updateStuAnswer(Map<String, Object> params);
}
