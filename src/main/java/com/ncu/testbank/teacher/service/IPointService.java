package com.ncu.testbank.teacher.service;

import java.util.List;

import com.ncu.testbank.teacher.data.Point;

public interface IPointService {
	/**
	 * 添加考点，ID自生成
	 * 
	 * @param point
	 */
	public void insertPoint(Point point);

	/**
	 * 刪除考点
	 * 
	 * @param point_id
	 */
	public void deletePoint(List<Long> point_id);

	/**
	 * 更新考点
	 * 
	 * @param point
	 */
	public void updatePoint(Point point);

	/**
	 * 获取考点
	 * 
	 * @param point_id
	 * @return
	 */
	public Point getPoint(long point_id);

	/**
	 * 通过课程ID查询考点
	 * 
	 * @param course_id
	 * @return
	 */
	public List<Point> searchPoint(String course_id);

}
