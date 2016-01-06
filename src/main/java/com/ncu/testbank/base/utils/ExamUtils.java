package com.ncu.testbank.base.utils;

import com.ncu.testbank.teacher.data.QuestionCount;
import com.ncu.testbank.teacher.data.QuestionLevel;
import com.ncu.testbank.teacher.data.Template;

public class ExamUtils {
	/**
	 * 初始化题目数量
	 * 
	 * @param total
	 *            题目总数
	 * @param template
	 *            题目模板，所用字段为easy_ratio, medium_ratio, hard_ratio
	 * @param hardCount
	 *            hard难度题目数量
	 * @param mediumCount
	 *            medium难度题目数量
	 * @param easyCount
	 *            easy难度题目数量
	 */
	public static QuestionCount initCount(Integer total, Template template) {
		double hard_ratio = template.getHard_ratio();
		double medium_ratio = template.getMedium_ratio();

		int hardCount = (int) (hard_ratio * total);
		int mediumCount = (int) (medium_ratio * total);
		int easyCount = total - hardCount - mediumCount;

		return new QuestionCount(hardCount, mediumCount, easyCount);
	}

	/**
	 * 初始化各个档次题目难度等级
	 * 
	 * @param level
	 * @param hard
	 * @param medium
	 * @param easy
	 */
	public static QuestionLevel initLevel(Integer level) {
		int hard = level;
		int medium = 0;
		int easy = 0;
		if (level - 1 >= 1) {
			medium = level - 1;
		} else {
			medium = 1;
		}

		if (level - 2 >= 1) {
			easy = level - 2;
		} else {
			easy = 1;
		}

		return new QuestionLevel(hard, medium, easy);
	}
}
