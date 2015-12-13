package com.ncu.testbank.admin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.admin.data.Course;

@Repository
public interface ICourseDao {

	public int getCount(Map<String, Object> params);

	public List<Course> searchData(Map<String, Object> params);

	public Course getCourse(String course_id);

	public int insertOne(Course course);

	public int deleteOne(String course_id);

	public void deleteData(List<String> course_id);

	public int updateOne(Course course);

	public void loadCsv(String file);
}
