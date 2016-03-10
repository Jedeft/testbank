package com.ncu.testbank.teacher.service;

import java.sql.Timestamp;

import com.ncu.testbank.teacher.data.Exam;

public interface IExamService {
	/**
	 * 智能组卷考试(主为后台程序调用)
	 * @param template_id
	 * @param user_id
	 * @param start
	 * @param end
	 * @return
	 */
	public Exam createExam(long template_id, String user_id, Timestamp start, Timestamp end);
}
