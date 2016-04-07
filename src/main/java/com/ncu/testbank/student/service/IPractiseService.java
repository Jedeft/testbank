package com.ncu.testbank.student.service;

import java.sql.Timestamp;
import java.util.List;

import com.ncu.testbank.student.data.Practise;
import com.ncu.testbank.teacher.data.Template;
import com.ncu.testbank.teacher.data.view.ExamPaperView;
import com.ncu.testbank.teacher.data.view.OnlineExamView;

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
	public List<OnlineExamView> searchBySID(String student_id,
			String course_id);

	/**
	 * 根据使用者ID查询试卷
	 * @param exam_id
	 * @return
	 */
	public ExamPaperView getExamByID(Long exam_id);
}
