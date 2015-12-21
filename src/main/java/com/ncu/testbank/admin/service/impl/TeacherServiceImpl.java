package com.ncu.testbank.admin.service.impl;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import com.ncu.testbank.base.utils.CsvReader;
import com.ncu.testbank.base.utils.CsvWriter;
import com.ncu.testbank.permission.dao.IUserDao;
import com.ncu.testbank.permission.data.User;

@Service("teacherService")
public class TeacherServiceImpl implements ITeacherService {

	@Autowired
	private ITeacherDao teacherDao;

	@Autowired
	private IUserDao userDao;

	@Override
	public List<Teacher> searchData(PageInfo page, Teacher teacher)
			throws IllegalAccessException, InstantiationException,
			InvocationTargetException, IntrospectionException {
		Map<String, Object> params = BeanToMapUtils.convertBean(teacher);
		int count = teacherDao.getCount(params);
		page.setTotal(count);
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		}
		page.setTotalPage(count / page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的教师！"));
		}
		// 数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return teacherDao.searchData(params);
	}

	@Override
	public void insertOne(Teacher teacher) {
		if (teacherDao.insertOne(teacher) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "添加教师信息失败，请联系管理人员！"));
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
		user.setUsername(teacher.getTeacher_id());
		user.setPassword(config.getProperty("defaultPassword"));
		user.setSecond_pwd(config.getProperty("defaultSecond_pwd"));
		user.setName(teacher.getName());

		if (userDao.insertOne(user) < 1) {
			throw new ServiceException(new ErrorCode(30002,
					"添加教师用户失败，请删除掉该教师后重试！"));
		}
	}

	@Override
	public void deleteOne(String teacher_id) {
		if (teacherDao.deleteOne(teacher_id) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "删除教师信息失败，请联系管理人员！"));
		}
		if (userDao.deleteOne(teacher_id) < 1) {
			throw new ServiceException(new ErrorCode(30003, "教师账号不存在，账号删除失败！"));
		}
	}

	@Override
	public void updateOne(Teacher teacher) {
		if (teacherDao.updateOne(teacher) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "更新教师信息失败，请联系管理人员！"));
		}
		User user = new User();
		user.setUsername(teacher.getTeacher_id());
		user.setName(teacher.getName());
		if (userDao.updateOne(user) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "教师账号不存在，账号信息更新失败！"));
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
			if (!target.getParentFile().mkdirs()) {
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
		}
		if (!target.createNewFile()) {
			throw new ServiceException(ErrorCode.FILE_IO_ERROR);
		}

		file.transferTo(target);
		teacherDao.loadCsv(target.getPath());

		// 录入教师信息到用户表中
		Properties config = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("accountInfo.properties");
		try {
			config.load(in);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException(ErrorCode.FILE_PROPERTIES_ERROR);
		}
		String defaultPassword = config.getProperty("defaultPassword");
		String defaultSecond_pwd = config.getProperty("defaultSecond_pwd");
		CsvReader cr = new CsvReader(target.getPath());
		List<User> userList = new ArrayList<>();
		while (cr.readRecord()) {
			String values[] = cr.getValues();
			User user = new User();
			user.setUsername(values[0]);
			user.setName(values[2]);
			user.setPassword(defaultPassword);
			user.setSecond_pwd(defaultSecond_pwd);

			userList.add(user);
		}
		cr.close();

		String outFileName = path + "\\account_" + fileName;
		CsvWriter cw = new CsvWriter(outFileName);
		for (User user : userList) {
			String[] values = new String[4];
			values[0] = user.getUsername();
			values[1] = user.getPassword();
			values[2] = user.getName();
			values[3] = user.getSecond_pwd();
			cw.writeRecord(values);
		}
		cw.close();
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
	public void deleteData(List<String> teacher_id) {
		teacherDao.deleteData(teacher_id);
		userDao.deleteData(teacher_id);
	}

}
