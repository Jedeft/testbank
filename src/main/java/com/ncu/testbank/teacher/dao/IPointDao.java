package com.ncu.testbank.teacher.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.Point;

@Repository
public interface IPointDao {
	public int insertOne(Point point);

	public int deleteData(List<Long> point_id);
	
	public int updateOne(Point point);
	
	public Point getPoint(long point_id);
	
	/**
	 * 通过课程ID，检索该课程下所有的考点
	 * @param course_id
	 * @return
	 */
	public List<Point> searchData(String course_id);
}
