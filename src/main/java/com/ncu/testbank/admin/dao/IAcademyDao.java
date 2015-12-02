package com.ncu.testbank.admin.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.admin.data.Academy;
import com.ncu.testbank.base.response.PageInfo;

@Repository
public interface IAcademyDao {
	
	public int getCount();
	
	public List<Academy> searchData(PageInfo page);

	public Academy getAcademy(String academy_id);
	
	public int insertOne(Academy academy);
	
	public int deleteOne(String academy_id);
	
	public int updateOne(Academy academy);
	
	public void loadCsv(String file);
}
