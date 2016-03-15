package com.ncu.testbank.teacher.dao;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.Exam;

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
}
