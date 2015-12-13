package com.ncu.testbank.admin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.admin.data.Academy;

@Repository
public interface IAcademyDao {

	public int getCount(Map<String, Object> params);

	public List<Academy> searchData(Map<String, Object> params);

	public Academy getAcademy(String academy_id);

	public int insertOne(Academy academy);

	public int deleteOne(String academy_id);

	public void deleteData(List<String> academy_id);

	public int updateOne(Academy academy);

	public void loadCsv(String file);
}
