package com.ncu.testbank.admin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.admin.data.Academy;
import com.ncu.testbank.admin.data.Teacher;

@Repository
public interface IBankBuilderDao {
	
	public int getCount(Map<String, Object> params);
	
	public List<Teacher> searchData(Map<String, Object> params);

	public Teacher getBankBuilder(String teacher_id);
	
	public int insertOne(String teacher_id);
	
	public int deleteOne(String teacher_id);
	
	public void deleteData(List<String> teacher_id);
}
