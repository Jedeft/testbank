package com.ncu.testbank.admin.service.impl;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
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
import com.ncu.testbank.base.utils.CsvReader;
import com.ncu.testbank.base.utils.CsvWriter;
import com.ncu.testbank.permission.dao.IUserDao;
import com.ncu.testbank.permission.data.User;

@Service("studentService")
public class StudentServiceImpl implements IStudentService {

	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private IStudentDao studentDao;

	@Autowired
	private IUserDao userDao;

	@Override
	public List<Student> searchData(PageInfo page, Student student) {
		Map<String, Object> params = null;
		try {
			params = BeanToMapUtils.convertBean(student);
		} catch (IllegalAccessException | InvocationTargetException
				| IntrospectionException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.MAP_CONVERT_ERROR);
		}
		int count = studentDao.getCount(params);
		page.setTotal(count);
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		}
		page.setTotalPage(count / page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的学生！"));
		}
		// 数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return studentDao.searchData(params);
	}

	@Override
	public void insertOne(Student student) {
		if (studentDao.insertOne(student) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "添加学生信息失败，请联系管理人员！"));
		}
		Properties config = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("accountInfo.properties");
		try {
			config.load(in);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException(ErrorCode.FILE_PROPERTIES_ERROR);
		}

		User user = new User();
		user.setUsername(student.getStudent_id());
		user.setPassword(config.getProperty("defaultPassword"));
		user.setSecond_pwd(config.getProperty("defaultSecond_pwd"));
		user.setName(student.getName());

		if (userDao.insertOne(user) < 1) {
			throw new ServiceException(new ErrorCode(30002,
					"添加学生用户失败，请删除掉该学生后重试！"));
		}
	}

	@Override
	public void deleteOne(String student_id) {
		if (studentDao.deleteOne(student_id) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "删除学生信息失败，请联系管理人员！"));
		}
		if (userDao.deleteOne(student_id) < 1) {
			throw new ServiceException(new ErrorCode(30003, "学生账号不存在，账号删除失败！"));
		}

	}

	@Override
	public void updateOne(Student student) {
		if (studentDao.updateOne(student) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "更新学生信息失败，请联系管理人员！"));
		}
		if (student.getName() != null) {
			User user = new User();
			user.setUsername(student.getStudent_id());
			user.setName(student.getName());
			if (userDao.updateOne(user) < 1) {
				throw new ServiceException(
						new ErrorCode(30001, "学生账号不存在，账号信息更新失败！"));
			}
		}
	}

	@Override
	public Student getStudent(String student_id) {
		return studentDao.getStudent(student_id);
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
		studentDao.loadCsv(target.getPath());

		// 录入学生信息到用户表中
		Properties config = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("accountInfo.properties");
		try {
			config.load(in);
		} catch (IOException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.FILE_PROPERTIES_ERROR);
		}
		String defaultPassword = config.getProperty("defaultPassword");
		String defaultSecond_pwd = config.getProperty("defaultSecond_pwd");
		CsvReader cr;
		try {
			cr = new CsvReader(target.getPath());
		} catch (FileNotFoundException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.FILE_NOTFOUND);
		}
		List<User> userList = new ArrayList<>();
		try {
			while (cr.readRecord()) {
				String values[] = cr.getValues();
				User user = new User();
				user.setUsername(values[0]);
				user.setName(values[2]);
				user.setPassword(defaultPassword);
				user.setSecond_pwd(defaultSecond_pwd);

				userList.add(user);
			}
		} catch (IOException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.FILE_IO_ERROR);
		} finally {
			if (cr != null) {
				cr.close();
			}
		}

		String outFileName = path + "\\account_" + fileName;
		CsvWriter cw = new CsvWriter(outFileName);
		try {
			for (User user : userList) {
				String[] values = new String[4];
				values[0] = user.getUsername();
				values[1] = user.getPassword();
				values[2] = user.getName();
				values[3] = user.getSecond_pwd();

				cw.writeRecord(values);
			}
		} catch (IOException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.FILE_IO_ERROR);
		} finally {
			if (cw != null) {
				cw.close();
			}
		}
		userDao.loadCsv(outFileName);

		File outCsv = new File(outFileName);
		if (target.exists()) {
			target.delete();
		}
		if (outCsv.exists()) {
			outCsv.delete();
		}
	}

	@Override
	public void deleteData(List<String> student_id) {
		studentDao.deleteData(student_id);
		userDao.deleteData(student_id);
	}
}
