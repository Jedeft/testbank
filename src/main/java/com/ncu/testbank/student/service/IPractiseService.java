package com.ncu.testbank.student.service;

import java.sql.Timestamp;

import com.ncu.testbank.teacher.data.Exam;
import com.ncu.testbank.teacher.data.Template;

public interface IPractiseService {
	/**
	 * 智能组卷考试
	 * 
	 * @param template
	 * @param user_id
	 * @param start
	 * @param end
	 * @return
	 */
	public Exam createPractise(Template template, String student_id, Timestamp start,
			Timestamp end);
}
