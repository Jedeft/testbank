package com.ncu.testbank.admin.service;

import java.util.List;

import com.ncu.testbank.admin.data.Academy;
import com.ncu.testbank.base.response.PageInfo;

public interface IAcademyService {
	
	/**
	 * 检索academy
	 * @param academy
	 * @return
	 */
	public List<Academy> searchData(PageInfo page);
	
	/**
	 * 插入一条academy
	 * @param academy
	 */
	public void insertOne(Academy academy);
	
	/**
	 * 删除一条academy
	 * @param academy_id
	 */
	public void deleteOne(String academy_id);
	
	/**
	 * 更新一条academy
	 * @param academy
	 */
	public void updateOne(Academy academy);
	
	/**
	 * 获取一条academy
	 * @param academy_id
	 */
	public Academy getAcademy(String academy_id);
}
