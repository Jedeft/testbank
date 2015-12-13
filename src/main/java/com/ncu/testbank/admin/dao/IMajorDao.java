package com.ncu.testbank.admin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.admin.data.Major;

@Repository
public interface IMajorDao {
	public int getCount(Map<String, Object> params);

	public List<Major> searchData(Map<String, Object> params);

	public Major getMajor(String major_id);

	public int insertOne(Major major);

	public int deleteOne(String major_id);

	public void deleteData(List<String> major_id);

	public int updateOne(Major major);

	public void loadCsv(String file);
}
