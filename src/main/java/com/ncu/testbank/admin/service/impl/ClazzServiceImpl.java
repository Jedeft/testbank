package com.ncu.testbank.admin.service.impl;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.dao.IClazzDao;
import com.ncu.testbank.admin.data.Clazz;
import com.ncu.testbank.admin.service.IClazzService;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.BeanToMapUtils;

@Service("clazzService")
public class ClazzServiceImpl implements IClazzService {

	@Autowired
	private IClazzDao clazzDao;

	@Override
	public List<Clazz> searchData(PageInfo page, Clazz clazz)
			throws IllegalAccessException, InstantiationException,
			InvocationTargetException, IntrospectionException {
		Map<String, Object> params = BeanToMapUtils.convertBean(clazz);
		int count = clazzDao.getCount(params);
		page.setTotal(count);
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		}
		page.setTotalPage(count / page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的班级！"));
		}
		// 数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return clazzDao.searchData(params);
	}

	@Override
	public void insertOne(Clazz clazz) {
		if (clazzDao.insertOne(clazz) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "添加班级信息失败，请联系管理人员！"));
		}
	}

	@Override
	public void deleteOne(String class_id) {
		if (clazzDao.deleteOne(class_id) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "删除班级信息失败，请联系管理人员！"));
		}
	}

	@Override
	public void updateOne(Clazz clazz) {
		if (clazzDao.updateOne(clazz) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "更新班级信息失败，请联系管理人员！"));
		}
	}

	@Override
	public Clazz getClazz(String class_id) {
		return clazzDao.getClazz(class_id);
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
		clazzDao.loadCsv(target.getPath());
		if (target.exists()) {
			target.delete();
		}
	}

	@Override
	public void deleteData(List<String> class_id) {
		clazzDao.deleteData(class_id);
	}

}
