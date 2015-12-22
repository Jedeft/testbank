package com.ncu.testbank.teacher.service;

import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.data.Single;

public interface ISingleService {
	/**
	 * 插入文字题目
	 * @param single
	 * @param user 当前操作用户信息
	 */
	public void insertWriting(Single single, User user);
}
