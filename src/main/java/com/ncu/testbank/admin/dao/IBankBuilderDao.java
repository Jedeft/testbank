package com.ncu.testbank.admin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.admin.data.BankBuilder;
import com.ncu.testbank.admin.data.Course;
import com.ncu.testbank.admin.data.view.BankBuilderView;

@Repository
public interface IBankBuilderDao {

	public int getCount(Map<String, Object> params);

	public List<BankBuilderView> searchData(Map<String, Object> params);
	
	public List<Course> searchCourseByTID(String teacher_id);

	public int insertOne(BankBuilder bankBuilder);

	public int deleteOne(BankBuilder bankBuilder);
}
