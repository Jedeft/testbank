package com.ncu.testbank.admin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.admin.data.Student;

@Repository
public interface IStudentDao {

	public int getCount(Map<String, Object> params);

	public List<Student> searchData(Map<String, Object> params);

	public Student getStudent(String student_id);

	/**
	 * 通过student_id集合获取学生集合
	 * 
	 * @param student_id
	 * @return
	 */
	public List<Student> getStudents(List<String> student_id);

	public int insertOne(Student student);

	public int deleteOne(String student_id);

	public void deleteData(List<String> student_id);

	public int updateOne(Student student);

	public void loadCsv(String file);
}
