package com.ncu.testbank.teacher.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.data.ShortAnswer;
import com.ncu.testbank.teacher.data.params.DELQuestionParams;
import com.ncu.testbank.teacher.data.view.ShortAnswerExamView;
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
	 * 根据考试ID获取简答题（结果集无标准答案）
	 * 
	 * @param exam_id
	 * @return
	 */
	public List<ShortAnswerExamView> searchExamShortNoAnswer(Long exam_id);
}
