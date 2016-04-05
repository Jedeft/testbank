package com.ncu.testbank.teacher.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.SyllabusStudent;
import com.ncu.testbank.teacher.data.view.SyllabusStudentView;

@Repository
public interface ITeachingStudentDao {
	public int getCount(Map<String, Object> params);

	public List<SyllabusStudentView> searchData(Map<String, Object> params);

	public int insertOne(SyllabusStudent teachingStudent);

	public int deleteOne(SyllabusStudent teachingStudent);
}
