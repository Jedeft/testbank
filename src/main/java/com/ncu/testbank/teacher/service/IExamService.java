package com.ncu.testbank.teacher.service;

import java.sql.Timestamp;
import java.util.List;

import com.ncu.testbank.teacher.data.Exam;
import com.ncu.testbank.teacher.data.Judge;
import com.ncu.testbank.teacher.data.Multiple;
import com.ncu.testbank.teacher.data.ShortAnswer;
import com.ncu.testbank.teacher.data.Single;
import com.ncu.testbank.teacher.data.Template;
import com.ncu.testbank.teacher.data.view.ExamPaperView;
import com.ncu.testbank.teacher.data.view.HistoryExamView;
import com.ncu.testbank.teacher.data.view.OnlineExamView;

public interface IExamService {
	/**
	 * 智能组卷考试(主为后台程序调用)
	 * 
	 * @param template
	 * @param user_id
	 * @param start
	 * @param end
	 * @return
	 */
	public Exam createExam(Template template, String user_id, Timestamp start,
			Timestamp end);

	/**
	 * 删除单选题
	 * 
	 * @param exam_id
	 * @param question_id
	 */
	public void deleteSingle(Long exam_id, Long question_id);

	/**
	 * 删除多选题
	 * 
	 * @param exam_id
	 * @param question_id
	 */
	public void deleteMultiple(Long exam_id, Long question_id);

	/**
	 * 删除判断题
	 * 
	 * @param exam_id
	 * @param question_id
	 */
	public void deleteJudge(Long exam_id, Long question_id);

	/**
	 * 删除简答题
	 * 
	 * @param exam_id
	 * @param question_id
	 */
	public void deleteShortAnswer(Long exam_id, Long question_id);

	/**
	 * 新增单选题
	 * 
	 * @param exam_id
	 * @param question_id
	 */
	public Single insertSingle(Long exam_id, Long question_id);

	/**
	 * 新增单选题
	 * 
	 * @param exam_id
	 * @param question_id
	 */
	public Multiple insertMultiple(Long exam_id, Long question_id);

	/**
	 * 新增单选题
	 * 
	 * @param exam_id
	 * @param question_id
	 */
	public Judge insertJudge(Long exam_id, Long question_id);

	/**
	 * 新增单选题
	 * 
	 * @param exam_id
	 * @param question_id
	 */
	public ShortAnswer insertShortAnswer(Long exam_id, Long question_id);

	/**
	 * 根据教师ID和课程ID查询在线考试情况
	 * 
	 * @param teacher_id
	 * @param course_id
	 * @return
	 */
	public List<OnlineExamView> searchOnlineByTID(String teacher_id,
			String course_id);
	
	/**
	 * 根据学生ID查询在线考试情况
	 * 
	 * @param student_id
	 * @return
	 */
	public List<OnlineExamView> searchOnlineBySID(String student_id);

	/**
	 * 根据教师ID和课程ID查询历史笔试卷情况
	 * 
	 * @param teacher_id
	 * @param course_id
	 * @return
	 */
	public List<HistoryExamView> searchHistoryByTID(String teacher_id,
			String course_id);
	
	/**
	 * 根据试卷ID获得试卷详细（结果集包含正确答案）
	 * @param exam_id
	 * @return
	 */
	public ExamPaperView getExamDetailByID(Long exam_id);
	
	/**
	 * 根据试卷ID获得试卷详细（结果集不包含正确答案）
	 * @param exam_id
	 * @return
	 */
	public ExamPaperView getExamDetailByIDNoAnswer(Long exam_id);
	
	/**
	 * 根据试卷ID获得试卷基本信息
	 * @param exam_id
	 * @return
	 */
	public Exam getExamByID(Long exam_id);
	
	/**
	 * 自动改卷
	 * 更新考试状态，置为已提交，后续不可修改
	 * 此处开启事物
	 * @param exam_id
	 */
	public void AutoCheckExam(Long exam_id);
}
