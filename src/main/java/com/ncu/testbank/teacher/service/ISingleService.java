package com.ncu.testbank.teacher.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.data.Single;
import com.ncu.testbank.teacher.data.params.DELQuestionParams;
import com.ncu.testbank.teacher.data.view.SingleView;

public interface ISingleService {
	/**
	 * 插入文字题目
	 * 
	 * @param single
	 * @param user
	 *            当前操作用户信息
	 */
	public void insertWriting(Single single, User user);

	/**
	 * 修改文字题目
	 * 
	 * @param single
	 * @param user
	 */
	public void updateWriting(Single single, User user);

	/**
	 * 检索题目
	 * 
	 * @param pageInfo
	 * @param single
	 * @return
	 */
	public List<SingleView> searchData(PageInfo pageInfo, Single single);

	/**
	 * 获取题目详细信息
	 * 
	 * @param question_id
	 * @return
	 */
	public Single getSingle(long question_id);

	/**
	 * 删除题目
	 * 
	 * @param question_id
	 */
	public void deleteQuestion(List<DELQuestionParams> question_id);

	/**
	 * 插入图片题目
	 * 
	 * @param single
	 * @param user
	 * @param file
	 */
	public void insertImge(Single single, User user, MultipartFile file);

	/**
	 * 修改图片题目
	 * 
	 * @param single
	 * @param user
	 * @param file
	 */
	public void updateImge(Single single, User user, MultipartFile file);
	
	/**
	 * 更新考试学生答案
	 * @param exam_id
	 * @param question_id
	 * @param answer
	 */
	public void updateExamStuAnswer(Long exam_id, Long question_id, String answer);
}
