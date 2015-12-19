package com.ncu.testbank.teacher.dao;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.TeachingStudent;

@Repository
public interface ITeachingStudentDao {
	
	public int insertOne(TeachingStudent teachingStudent);
}
