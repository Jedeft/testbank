package com.ncu.testbank.teacher.service;

import java.sql.Timestamp;

public interface IExamService {
	/**
	 * 智能组卷考试(主为后台程序调用)
	 * @param template_id
	 * @param user_id
	 * @param start
	 * @param end
	 */
	public void createExam(long template_id, String user_id, Timestamp start, Timestamp end);
}
