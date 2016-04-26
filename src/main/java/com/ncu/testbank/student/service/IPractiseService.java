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
	public Practise createPractise(Template template, String student_id,
			Timestamp start, Timestamp end);

	/**
	 * 根据学生ID查询在线练习情况
	 * 
	 * @param student_id
	 * @return
	 */
	public List<PractiseView> searchData(String student_id);

	/**
	 * 根据试卷ID获得试卷详细（结果集包含正确答案）
	 * 
	 * @param practise_id
	 * @return
	 */
	public PractisePaperView getPractiseDetailByID(Long practise_id);

	/**
	 * 根据练习ID获取无答案练习卷
	 * 
	 * @param practise_id
	 * @return
	 */
	public PractisePaperView getPracitseDetailByIDNoAnswer(Long practise_id);

	/**
	 * 根据练习ID获得练习基本信息
	 * 
	 * @param practise_id
	 * @return
	 */
	public Practise getPractiseByID(Long practise_id);

	/**
	 * 自动改卷
	 * 更新练习状态，置为已提交，后续不可修改
	 * 此处开启事物
	 * @param practise_id
	 */
	public void AutoCheckPractise(Long practise_id);
}
