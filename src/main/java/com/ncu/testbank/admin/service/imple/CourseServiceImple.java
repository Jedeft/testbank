package com.ncu.testbank.admin.service.imple;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.dao.ICourseDao;
import com.ncu.testbank.admin.data.Course;
import com.ncu.testbank.admin.service.ICourseService;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.BeanToMapUtils;

@Service("courseService")
public class CourseServiceImple implements ICourseService {

	@Autowired
	private ICourseDao courseDao;

	@Override
	public List<Course> searchData(PageInfo page, Course course)
			throws IllegalAccessException, InstantiationException,
			InvocationTargetException, IntrospectionException {
		Map<String, Object> params = BeanToMapUtils.convertBean(course);
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
	public void loadCsv(String fileName, String path, MultipartFile file)
			throws IllegalStateException, IOException {
		File target = new File(path, fileName);
		if (!target.getParentFile().exists()) {
			if (!target.getParentFile().mkdirs()) {
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
		}
		if (!target.createNewFile()) {
			throw new ServiceException(ErrorCode.FILE_IO_ERROR);
		}
		file.transferTo(target);
		courseDao.loadCsv(target.getPath());
		if (target.exists()) {
			target.delete();
		}
	}

	@Override
	public void deleteData(List<String> course_id) {
		courseDao.deleteData(course_id);
	}

}
