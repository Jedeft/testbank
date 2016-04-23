package com.ncu.testbank.student.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ncu.testbank.student.data.Practise;
import com.ncu.testbank.student.data.view.PractiseView;

@Repository
public interface IPractiseDao {
	/**
	 * 生成一个练习
	 * 
	 * @param practise
	 * @return
	 */
	public int insertOne(Practise practise);

	/**
	 * 删除一份练习
	 * 
	 * @param practise_id
	 * @return
	 */
	public int deleteOne(long practise_id);

	/**
	 * 根据学生ID获取练习情况
	 * 
	 * @param student_id
	 * @return
	 */
	public List<PractiseView> searchData(long student_id);

	/**
	 * 根据练习ID获得练习信息 
	 * 
	 * @param pracitse_id
	 * @return
	 */
	public Practise getPractiseById(Long practise_id);
	
	/**
	 * 更新练习状态
	 * @param exam_id
	 * @return
	 */
	public int updateStatus(Long practise_id);
}
