package com.ncu.testbank.teacher.dao;

import java.util.Date;
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
	 * 
	 * @param exam
	 * @return
	 */
	public int insertOne(Exam exam);

	/**
	 * 删除一份试卷
	 * 
	 * @param exam
	 * @return
	 */
	public int deleteOne(long exam_id);

	/**
	 * 根据教师ID和课程ID获得在线考试学生情况
	 * 
	 * @param params
	 * @return
	 */
	public List<OnlineExamView> searchOnlineExamByTID(Map<String, Object> params);

	/**
	 * 根据教师ID和课程ID获得在线考试学生情况
	 * 
	 * @param params
	 * @return
	 */
	public List<OnlineExamView> searchOnlineExamBySID(String student_id);

	/**
	 * 根据教师ID和课程ID获得历史试卷信息
	 * 
	 * @param params
	 * @return
	 */
	public List<HistoryExamView> searchHistoryExam(Map<String, Object> params);

	/**
	 * 根据试卷ID获得试卷详细信息
	 * 
	 * @param exam_id
	 * @return
	 */
	public Exam getExamById(Long exam_id);

	/**
	 * 更新试卷状态
	 * 
	 * @param exam_id
	 * @return
	 */
	public int updateStatus(Long exam_id);

	/**
	 * 更改试卷分数
	 * 
	 * @param params
	 * @return
	 */
	public int updateScore(Map<String, Object> params);

	/**
	 * 检索已过考试时间，未交卷的试卷
	 * 
	 * @param now
	 * @return
	 */
	public List<Exam> searchOverdueExam(Date now);
}
