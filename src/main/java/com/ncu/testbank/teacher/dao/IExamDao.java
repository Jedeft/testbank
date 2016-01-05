package com.ncu.testbank.teacher.dao;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.Exam;
import com.ncu.testbank.teacher.data.Question;

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
	 * 插入一道单选题入试卷-单选题联合表
	 * 
	 * @return
	 */
	public int insertSingle(Question question);

	/**
	 * 插入一道多选题入试卷-多选题联合表
	 * 
	 * @return
	 */
	public int insertMultiple(Question question);

	/**
	 * 插入一道判断题入试卷-判断题联合表
	 * 
	 * @return
	 */
	public int insertJudge(Question question);

	/**
	 * 插入一道简答题入试卷-简答题联合表
	 * 
	 * @return
	 */
	public int insertShortAnswer(Question question);
}
