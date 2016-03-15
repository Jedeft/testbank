package com.ncu.testbank.teacher.service;

import java.sql.Timestamp;

import com.ncu.testbank.teacher.data.Exam;
import com.ncu.testbank.teacher.data.Judge;
import com.ncu.testbank.teacher.data.Multiple;
import com.ncu.testbank.teacher.data.ShortAnswer;
import com.ncu.testbank.teacher.data.Single;
import com.ncu.testbank.teacher.data.Template;

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
}
