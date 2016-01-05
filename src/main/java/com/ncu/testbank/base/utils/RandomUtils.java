package com.ncu.testbank.base.utils;

public class RandomUtils {
	//生成指定范围内的随机数
	public static int random(int min, int max) {
		return (int) Math.round(Math.random() * (max - min) + min);
	}
}
