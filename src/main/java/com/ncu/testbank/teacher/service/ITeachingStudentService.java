package com.ncu.testbank.teacher.service;

import java.util.List;

import com.ncu.testbank.teacher.data.TeachingStudent;

public interface ITeachingStudentService {
	/**
	 * 1.通过教师ID、课程ID以及当下时间来确定出课程表syllabus_id 
	 * 2.syllabus_id和student_id插入到授课表中
	 * @param teachingStudent 包含teacher_id, course_id, student_id
	 */
	public void insertStudents(String teacher_id, String course_id, List<String> student_id);
}
