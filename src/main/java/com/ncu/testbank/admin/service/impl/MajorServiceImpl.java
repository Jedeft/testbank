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

import com.ncu.testbank.admin.dao.IMajorDao;
import com.ncu.testbank.admin.data.Major;
import com.ncu.testbank.admin.service.IMajorService;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.BeanToMapUtils;

@Service("majorService")
public class MajorServiceImpl implements IMajorService {

	@Autowired
	private IMajorDao majorDao;

	@Override
	public List<Major> searchData(PageInfo page, Major major)
			throws IllegalAccessException, InstantiationException,
			InvocationTargetException, IntrospectionException {
		Map<String, Object> params = BeanToMapUtils.convertBean(major);
		int count = majorDao.getCount(params);
		page.setTotal(count);
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		}
		page.setTotalPage(count / page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的专业！"));
		}
		// 数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return majorDao.searchData(params);
	}

	@Override
	public void insertOne(Major major) {
		if (majorDao.insertOne(major) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "添加专业信息失败，请联系管理人员！"));
		}
	}

	@Override
	public void deleteOne(String major_id) {
		if (majorDao.deleteOne(major_id) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "删除专业信息失败，请联系管理人员！"));
		}
	}

	@Override
	public void updateOne(Major major) {
		if (majorDao.updateOne(major) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "更新专业信息失败，请联系管理人员！"));
		}
	}

	@Override
	public Major getMajor(String major_id) {
		return majorDao.getMajor(major_id);
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
		majorDao.loadCsv(target.getPath());
		if (target.exists()) {
			target.delete();
		}
	}

	@Override
	public void deleteData(List<String> major_id) {
		majorDao.deleteData(major_id);
	}

}
