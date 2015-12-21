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

import com.ncu.testbank.admin.dao.IAcademyDao;
import com.ncu.testbank.admin.data.Academy;
import com.ncu.testbank.admin.service.IAcademyService;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.BeanToMapUtils;

@Service("academyService")
public class AcademyServiceImpl implements IAcademyService {

	@Autowired
	private IAcademyDao academyDao;

	@Override
	public List<Academy> searchData(PageInfo page, Academy academy)
			throws IllegalAccessException, InvocationTargetException,
			IntrospectionException {
		Map<String, Object> params = BeanToMapUtils.convertBean(academy);
		int count = academyDao.getCount(params);
		page.setTotal(count);
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		}
		page.setTotalPage(count / page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的学院！"));
		}
		// 数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return academyDao.searchData(params);
	}

	@Override
	public void insertOne(Academy academy) {
		if (academyDao.insertOne(academy) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "添加学院信息失败，请联系管理人员！"));
		}
	}

	@Override
	public void deleteOne(String academy_id) {
		if (academyDao.deleteOne(academy_id) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "删除学院信息失败，请联系管理人员！"));
		}
	}

	@Override
	public void updateOne(Academy academy) {
		if (academyDao.updateOne(academy) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "更新学院信息失败，请联系管理人员！"));
		}
	}

	@Override
	public Academy getAcademy(String academy_id) {
		return academyDao.getAcademy(academy_id);
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
		academyDao.loadCsv(target.getPath());
		if (target.exists()) {
			target.delete();
		}
	}

	@Override
	public void deleteData(List<String> academy_id) {
		academyDao.deleteData(academy_id);
	}

}
