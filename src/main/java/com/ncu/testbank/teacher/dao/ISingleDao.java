package com.ncu.testbank.teacher.dao;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.teacher.data.Single;

@Repository
public interface ISingleDao {
	public int insertOne(Single single);
}
