package com.ncu.testbank.teacher.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.Exam;
import com.ncu.testbank.teacher.data.view.HistoryExamView;
import com.ncu.testbank.teacher.data.view.OnlineExamView;

@Repository
public interface IExamDao {
	/**
	 * 生成一个试卷
	 * @param exam
	 * @return
	 */
	public int insertOne(Exam exam);
	
	/**
	 * 删除一份试卷
	 * @param exam
	 * @return
	 */
	public int deleteOne(long exam_id);
	
	/**
	 * 根据教师ID和课程ID获得在线考试学生情况
	 * @param params
	 * @return
	 */
	public List<OnlineExamView> searchOnlineExam(Map<String, Object> params);
	
	
	/**
	 * 根据教师ID和课程ID获得历史试卷信息
	 * @param params
	 * @return
	 */
	public List<HistoryExamView> searchHistoryExam(Map<String, Object> params);
	
	/**
	 * 根据试卷ID获得试卷信息
	 * 此处为一个事物
	 * @param params
	 * @return
	 */
	public Exam getExamById(Long user_id);
}
