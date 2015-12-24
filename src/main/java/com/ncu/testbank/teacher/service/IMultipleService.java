package com.ncu.testbank.teacher.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.data.Multiple;
import com.ncu.testbank.teacher.data.params.DELQuestionParams;
import com.ncu.testbank.teacher.data.view.MultipleView;

public interface IMultipleService {
	/**
	 * 插入文字题目
	 * 
	 * @param multiple
	 * @param user
	 *            当前操作用户信息
	 */
	public void insertWriting(Multiple multiple, User user);

	/**
	 * 修改文字题目
	 * 
	 * @param multiple
	 * @param user
	 */
	public void updateWriting(Multiple multiple, User user);

	/**
	 * 检索题目
	 * 
	 * @param pageInfo
	 * @param multiple
	 * @return
	 */
	public List<MultipleView> searchData(PageInfo pageInfo, Multiple multiple);

	/**
	 * 获取题目详细信息
	 * 
	 * @param question_id
	 * @return
	 */
	public Multiple getMultiple(long question_id);

	/**
	 * 删除题目
	 * 
	 * @param question_id
	 */
	public void deleteQuestion(List<DELQuestionParams> question_id);

	/**
	 * 插入图片题目
	 * 
	 * @param multiple
	 * @param user
	 * @param file
	 */
	public void insertImge(Multiple multiple, User user, MultipartFile file);

	/**
	 * 修改图片题目
	 * 
	 * @param multiple
	 * @param user
	 * @param file
	 */
	public void updateImge(Multiple multiple, User user, MultipartFile file);
}
