package com.ncu.testbank.admin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.admin.data.Syllabus;
import com.ncu.testbank.admin.data.view.SyllabusView;

@Repository
public interface ISyllabusDao {
	
	public int getCount(Map<String, Object> params);
	
	public List<SyllabusView> searchData(Map<String, Object> params);
	
	public int insertOne(Syllabus syllabus);
	
	public int updateOne(Syllabus syllabus);
	
	public SyllabusView getSyllabus(String syllabus_id);
	
	public int deleteOne(String syllabus_id);

	public void deleteData(List<String> syllabus_id);
	
	public void loadCsv(String file);
}
