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

import com.ncu.testbank.admin.dao.IStudentDao;
import com.ncu.testbank.admin.data.Student;
import com.ncu.testbank.admin.service.IStudentService;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.BeanToMapUtils;

@Service("studentService")
public class StudentServiceImple implements IStudentService{
	@Autowired
	private IStudentDao studentDao;
	
	@Override
	public List<Student> searchData(PageInfo page, Student student) throws IllegalAccessException, InstantiationException, InvocationTargetException, IntrospectionException {
		Map<String, Object> params = BeanToMapUtils.convertBean(student);
		int count = studentDao.getCount(params);
		page.setTotal(count);
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		} 
		page.setTotalPage(count/page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的学生！"));
		}
		//数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return studentDao.searchData(params);
	}

	@Override
	public void insertOne(Student student) {
		if ( studentDao.insertOne(student) < 1 ) {
			throw new ServiceException(new ErrorCode(30001, "添加学生信息失败，请重试！"));
		}
	}

	@Override
	public void deleteOne(String student_id) {
		if ( studentDao.deleteOne(student_id) < 1 ) {
			throw new ServiceException(new ErrorCode(30001, "删除学生信息失败，请重试！"));
		}
	}

	@Override
	public void updateOne(Student student) {
		if ( studentDao.updateOne(student) < 1 ) {
			throw new ServiceException(new ErrorCode(30001, "更新学生信息失败，请重试！"));
		}
	}

	@Override
	public Student getStudent(String student_id) {
		return studentDao.getStudent(student_id);
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
		studentDao.loadCsv(target.getPath());
		if (target.exists()) {
			target.delete();
		}
	}
}
