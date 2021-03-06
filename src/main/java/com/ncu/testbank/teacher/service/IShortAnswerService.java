package com.ncu.testbank.teacher.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.data.ShortAnswer;
import com.ncu.testbank.teacher.data.params.DELQuestionParams;
import com.ncu.testbank.teacher.data.view.ShortAnswerView;

public interface IShortAnswerService {
	/**
	 * 插入文字题目
	 * 
	 * @param shortAnswer
	 * @param user
	 *            当前操作用户信息
	 */
	public void insertWriting(ShortAnswer shortAnswer, User user);

	/**
	 * 修改文字题目
	 * 
	 * @param shortAnswer
	 * @param user
	 */
	public void updateWriting(ShortAnswer shortAnswer, User user);

	/**
	 * 检索题目
	 * 
	 * @param pageInfo
	 * @param shortAnswer
	 * @return
	 */
	public List<ShortAnswerView> searchData(PageInfo pageInfo,
			ShortAnswer shortAnswer);

	/**
	 * 获取题目详细信息
	 * 
	 * @param question_id
	 * @return
	 */
	public ShortAnswer getShortAnswer(long question_id);

	/**
	 * 删除题目
	 * 
	 * @param question_id
	 */
	public void deleteQuestion(List<DELQuestionParams> question_id);

	/**
	 * 插入图片题目
	 * 
	 * @param shortAnswer
	 * @param user
	 * @param questionFile
	 * @param answerFile
	 */
	public void insertImge(ShortAnswer shortAnswer, User user,
			MultipartFile questionFile, MultipartFile answerFile);

	/**
	 * 修改图片题目
	 * 
	 * @param shortAnswer
	 * @param user
	 * @param questionFile
	 * @param answerFile
	 */
	public void updateImge(ShortAnswer shortAnswer, User user,
			MultipartFile questionFile, MultipartFile answerFile);

	/**
	 * 更新考试学生答案
	 * 
	 * @param exam_id
	 * @param question_id
	 * @param answer
	 */
	public void updateExamStuAnswer(Long exam_id, Long question_id,
			String answer);

	/**
	 * 更新试卷简答题分数，并修改试卷总分数，开启事物
	 * 
	 * @param exam_id
	 * @param question_id
	 * @param score
	 */
	public void updateStuAnswerScore(Long exam_id, Long question_id,
			Double score);
}
