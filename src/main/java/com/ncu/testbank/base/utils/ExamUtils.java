package com.ncu.testbank.base.utils;

import com.ncu.testbank.teacher.data.Template;

public class ExamUtils {
	/**
	 * 初始化题目数量
	 * @param total 题目总数
	 * @param template 题目模板，所用字段为easy_ratio, medium_ratio, hard_ratio
	 * @param hard hard难度题目数量
	 * @param medium medium难度题目数量
	 * @param easy easy难度题目数量
	 */
	public static void initCount(Integer total, Template template, Integer hard, Integer medium, Integer easy){
		double hard_ratio = template.getHard_ratio();
		double medium_ratio = template.getMedium_ratio();
		
		hard = (int) (hard_ratio * total);
		medium = (int) (medium_ratio * total);
		easy = total - hard - medium;
	}
}
