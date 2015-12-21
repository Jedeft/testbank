package com.ncu.testbank.teacher.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.TeachingStudent;
import com.ncu.testbank.teacher.data.view.TeachingStudentView;

@Repository
public interface ITeachingStudentDao {
	public int getCount(Map<String, Object> params);

	public List<TeachingStudentView> searchData(Map<String, Object> params);

	public int insertOne(TeachingStudent teachingStudent);

	public int deleteOne(TeachingStudent teachingStudent);
}
