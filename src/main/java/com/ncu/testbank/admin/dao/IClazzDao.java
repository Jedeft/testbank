package com.ncu.testbank.admin.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.admin.data.Clazz;

@Repository
public interface IClazzDao {

	public int getCount(Map<String, Object> params);

	public List<Clazz> searchData(Map<String, Object> params);

	public Clazz getClazz(String clazz_id);

	public int insertOne(Clazz clazz);

	public int deleteOne(String clazz_id);

	public void deleteData(List<String> class_id);

	public int updateOne(Clazz clazz);

	public void loadCsv(String file);
}
