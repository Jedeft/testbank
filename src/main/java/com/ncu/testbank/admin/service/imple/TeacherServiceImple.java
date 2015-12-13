package com.ncu.testbank.admin.service.imple;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.dao.ITeacherDao;
import com.ncu.testbank.admin.data.Teacher;
import com.ncu.testbank.admin.service.ITeacherService;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.BeanToMapUtils;

@Service("teacherService")
public class TeacherServiceImple implements ITeacherService{

	@Autowired
	private ITeacherDao teacherDao;
	
	@Override
	public List<Teacher> searchData(PageInfo page, Teacher teacher) throws IllegalAccessException, InstantiationException, InvocationTargetException, IntrospectionException {
		Map<String, Object> params = BeanToMapUtils.convertBean(teacher);
		int count = teacherDao.getCount(params);
		page.setTotal(count);
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		} 
		page.setTotalPage(count/page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的教师！"));
		}
		//数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return teacherDao.searchData(params);
	}

	@Override
	public void insertOne(Teacher teacher) {
		if ( teacherDao.insertOne(teacher) < 1 ) {
			throw new ServiceException(new ErrorCode(30001, "添加教师信息失败，请重试！"));
		}
	}

	@Override
	public void deleteOne(String teacher_id) {
		if ( teacherDao.deleteOne(teacher_id) < 1 ) {
			throw new ServiceException(new ErrorCode(30001, "删除教师信息失败，请重试！"));
		}
	}

	@Override
	public void updateOne(Teacher teacher) {
		if ( teacherDao.updateOne(teacher) < 1 ) {
			throw new ServiceException(new ErrorCode(30001, "更新教师信息失败，请重试！"));
		}
	}

	@Override
	public Teacher getTeacher(String teacher_id) {
		return teacherDao.getTeacher(teacher_id);
	}

	@Override
	public void loadCsv(String fileName, String path, MultipartFile file)
			throws IllegalStateException, IOException {
		File target = new File(path, fileName);
		if (!target.getParentFile().exists()) {
			if ( !target.getParentFile().mkdirs() ) {
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
		}
		if ( !target.createNewFile() ) {
			throw new ServiceException(ErrorCode.FILE_IO_ERROR);
		}
		
		file.transferTo(target);
		teacherDao.loadCsv(target.getPath());
		if (target.exists()) {
			target.delete();
		}
	}

	@Override
	public void deleteData(List<String> teacher_id) {
		teacherDao.deleteData(teacher_id);
	}

}
