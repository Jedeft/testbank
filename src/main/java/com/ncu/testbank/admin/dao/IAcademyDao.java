package com.ncu.testbank.admin.dao;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.admin.data.Academy;

@Repository
public interface IAcademyDao {

	public Academy getAcademy(String academy_id);
	
	public int insertOne(Academy academy);
	
	public int deleteOne(String academy_id);
	
	public int updateOne(Academy academy);
}
