package com.ncu.testbank.teacher.service;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.teacher.data.view.TeachingStudentView;

public interface ITeachingStudentService {
	/**
	 * 1.通过教师ID、课程ID以及当下时间来确定出课程表syllabus_id 2.syllabus_id和student_id插入到授课表中
	 * 
	 * @param teachingStudent
	 *            ：teacher_id, course_id, student_id
	 */
	public void insertStudents(String teacher_id, String course_id,
			List<String> student_id);

	/**
	 * 检索授课学生信息 1.通过教师ID、课程ID以及当下时间来确定出课程表syllabus_id
	 * 2.通过syllabus_id和当下时间以及其他检索信息查询授课学生
	 * 
	 * @param pageInfo
	 * @param teaStudentParams
	 *            : student_id, student_name, course_id
	 * @return
	 */
	public List<TeachingStudentView> searchData(PageInfo page,
			TeachingStudentView teachingStudentView, String teacher_id)
			throws IllegalAccessException, InvocationTargetException,
			IntrospectionException;

	/**
	 * 1.通过教师ID、课程ID以及当下时间来确定出课程表syllabus_id 2.通过syllabus_id和学生ID，删除授课学生
	 * 
	 * @param teacher_id
	 *            course_id student_id
	 */
	public void deleteStudents(String teacher_id, String course_id,
			List<String> student_id);
}
