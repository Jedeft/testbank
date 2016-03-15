package com.ncu.testbank.teacher.service;

import java.sql.Timestamp;

import com.ncu.testbank.teacher.data.Exam;
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
}
