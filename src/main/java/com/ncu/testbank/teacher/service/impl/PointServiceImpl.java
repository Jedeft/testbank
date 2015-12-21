package com.ncu.testbank.teacher.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.utils.RandomID;
import com.ncu.testbank.teacher.dao.IPointDao;
import com.ncu.testbank.teacher.data.Point;
import com.ncu.testbank.teacher.service.IPointService;

@Service("pointService")
public class PointServiceImpl implements IPointService {

	@Autowired
	private IPointDao pointDao;

	@Override
	public void insertPoint(Point point) {
		point.setPoint_id(RandomID.getID());
		if (pointDao.insertOne(point) < 1) {
			throw new ServiceException(new ErrorCode(30001, "添加考点失败，请联系管理人员！"));
		}
	}

	@Override
	public void deletePoint(List<Long> point_id) {
		pointDao.deleteData(point_id);
	}

	@Override
	public void updatePoint(Point point) {
		if (pointDao.updateOne(point) < 1) {
			throw new ServiceException(new ErrorCode(30001, "更新考点失败，请联系管理人员！"));
		}
	}
	
	@Override
	public Point getPoint(long point_id) {
		return pointDao.getPoint(point_id);
	}

	@Override
	public List<Point> searchPoint(String course_id) {
		return pointDao.searchData(course_id);
	}

	

}
