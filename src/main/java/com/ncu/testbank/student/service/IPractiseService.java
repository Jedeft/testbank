package com.ncu.testbank.student.service;

import java.sql.Timestamp;
import java.util.List;

import com.ncu.testbank.student.data.Practise;
import com.ncu.testbank.student.data.view.PractisePaperView;
import com.ncu.testbank.student.data.view.PractiseView;
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
	public Practise createPractise(Template template, String student_id, Timestamp start,
			Timestamp end);
	
	/**
	 * 根据课程ID和学生ID查询在线练习情况
	 * 
	 * @param student_id
	 * @param course_id
	 * @return
	 */
	public List<PractiseView> searchBySID(String student_id,
			String course_id);

	/**
	 * 根据练习ID获取无答案练习卷
	 * @param practise_id
	 * @return
	 */
	public PractisePaperView getPracitseDetailByIDNoAnswer(Long practise_id);
}
