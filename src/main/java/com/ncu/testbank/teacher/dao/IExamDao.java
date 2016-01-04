package com.ncu.testbank.teacher.dao;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.Exam;

@Repository
public interface IExamDao {
	public int insertOne(Exam exam); 
}
