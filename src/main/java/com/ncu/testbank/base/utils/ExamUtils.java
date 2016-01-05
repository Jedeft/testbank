package com.ncu.testbank.base.utils;

import com.ncu.testbank.teacher.data.Template;

public class ExamUtils {
	/**
	 * 初始化题目数量
	 * @param total 题目总数
	 * @param template 题目模板，所用字段为easy_ratio, medium_ratio, hard_ratio
	 * @param hardCount hard难度题目数量
	 * @param mediumCount medium难度题目数量
	 * @param easyCount easy难度题目数量
	 */
	public static void initCount(Integer total, Template template, Integer hardCount, Integer mediumCount, Integer easyCount){
		double hard_ratio = template.getHard_ratio();
		double medium_ratio = template.getMedium_ratio();
		
		hardCount = (int) (hard_ratio * total);
		mediumCount = (int) (medium_ratio * total);
		easyCount = total - hardCount - mediumCount;
	}
	
	
	/**
	 * 初始化各个档次题目难度等级
	 * @param level
	 * @param hard
	 * @param medium
	 * @param easy
	 */
	public static void initLevel(Integer level, Integer hard, Integer medium, Integer easy){
		hard = level;
		if (level - 1 >= 1) {
			medium = level - 1;
		} else {
			medium = 1;
		}
		
		if (level - 2 >= 1) {
			easy = level -2;
		} else {
			easy = 1;
		}
	}
}
