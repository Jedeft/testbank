package com.ncu.testbank.admin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.admin.data.Course;
import com.ncu.testbank.admin.data.Syllabus;
import com.ncu.testbank.admin.data.view.SyllabusView;

@Repository
public interface ISyllabusDao {

	public int getCount(Map<String, Object> params);

	public List<SyllabusView> searchData(Map<String, Object> params);

	public int insertOne(Syllabus syllabus);

	public int updateOne(Syllabus syllabus);

	public SyllabusView getSyllabus(String syllabus_id);

	/**
	 * 通过教师ID以及当下时间查找该教师所带课程信息
	 * 
	 * @param params
	 * @return
	 */
	public List<Course> searchCourseByTID(Map<String, Object> params);

	public int deleteOne(String syllabus_id);

	public void deleteData(List<String> syllabus_id);

	public void loadCsv(String file);

	/**
	 * 通过教师ID，课程ID以及当下时间查找当前正在使用的课程表
	 * 
	 * @param params
	 *            teacher_id, course_id, today
	 * @return
	 */
	public Syllabus getSyllabusNow(Map<String, Object> params);
}
