package com.ncu.testbank.admin.service.impl;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.dao.IBankBuilderDao;
import com.ncu.testbank.admin.dao.ICourseDao;
import com.ncu.testbank.admin.dao.ISyllabusDao;
import com.ncu.testbank.admin.data.Course;
import com.ncu.testbank.admin.service.ICourseService;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.BeanToMapUtils;

@Service("courseService")
public class CourseServiceImpl implements ICourseService {

	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private ICourseDao courseDao;

	@Autowired
	private ISyllabusDao syllabusDao;

	@Autowired
	private IBankBuilderDao bankBuilderDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Course> searchData(PageInfo page, Course course) {
		Map<String, Object> params = null;
		try {
			params = BeanToMapUtils.convertBean(course);
		} catch (IllegalAccessException | InvocationTargetException
				| IntrospectionException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.MAP_CONVERT_ERROR);
		}
		int count = courseDao.getCount(params);
		page.setTotal(count);
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		}
		page.setTotalPage(count / page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的课程！"));
		}
		// 数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return courseDao.searchData(params);
	}

	@Override
	public void insertOne(Course course) {
		if (courseDao.insertOne(course) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "添加课程信息失败，请联系管理人员！"));
		}
	}

	@Override
	public void deleteOne(String course_id) {
		if (courseDao.deleteOne(course_id) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "删除课程信息失败，请联系管理人员！"));
		}
	}

	@Override
	public void updateOne(Course course) {
		if (courseDao.updateOne(course) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "更新课程信息失败，请联系管理人员！"));
		}
	}

	@Override
	public Course getCourse(String course_id) {
		return courseDao.getCourse(course_id);
	}

	@Override
	public void loadCsv(String fileName, String path, MultipartFile file) {
		File target = new File(path, fileName);
		if (!target.getParentFile().exists()) {
			if (!target.getParentFile().mkdirs()) {
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
		}
		try {
			if (!target.createNewFile()) {
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
			file.transferTo(target);
		} catch (IOException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.FILE_IO_ERROR);
		}
		courseDao.loadCsv(target.getPath());
		if (target.exists()) {
			target.delete();
		}
	}

	@Override
	public void deleteData(List<String> course_id) {
		courseDao.deleteData(course_id);
	}

	@Override
	public List<Course> searchFromSyllabusByTID(String teacher_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("teacher_id", teacher_id);
		params.put("today", new Date());
		return syllabusDao.searchCourseByTID(params);
	}

	@Override
	public List<Course> searchFromBankBuilder(String teacher_id) {
		return bankBuilderDao.searchCourseByTID(teacher_id);
	}

	@Override
	public List<Course> searchFromSyllabusBySID(String student_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("student_id", student_id);
		params.put("today", new Date());
		return syllabusDao.searchCourseByTID(params);
	}

}
