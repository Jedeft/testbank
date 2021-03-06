package com.ncu.testbank.admin.service.impl;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.dao.ISyllabusDao;
import com.ncu.testbank.admin.data.Syllabus;
import com.ncu.testbank.admin.data.view.SyllabusView;
import com.ncu.testbank.admin.service.ISyllabusService;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.BeanToMapUtils;
import com.ncu.testbank.base.utils.CsvReader;
import com.ncu.testbank.base.utils.CsvWriter;
import com.ncu.testbank.base.utils.RandomID;

@Service("syllabusService")
public class SyllabusServiceImpl implements ISyllabusService {

	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private ISyllabusDao syllabusDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<SyllabusView> searchData(PageInfo page, Syllabus syllabus) {
		Map<String, Object> params = null;
		try {
			params = BeanToMapUtils.convertBean(syllabus);
		} catch (IllegalAccessException | InvocationTargetException
				| IntrospectionException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.MAP_CONVERT_ERROR);
		}
		int count = syllabusDao.getCount(params);
		page.setTotal(count);
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		}
		page.setTotalPage(count / page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的课表！"));
		}
		// 数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return syllabusDao.searchData(params);
	}

	@Override
	public void insertOne(Syllabus syllabus) {
		syllabus.setSyllabus_id(RandomID.getID() + "");
		if (syllabusDao.insertOne(syllabus) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "添加课表信息失败，请联系管理人员！"));
		}
	}

	@Override
	public void deleteOne(String syllabus_id) {
		if (syllabusDao.deleteOne(syllabus_id) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "删除课表信息失败，请联系管理人员！"));
		}
	}

	@Override
	public void updateOne(Syllabus syllabus) {
		if (syllabusDao.updateOne(syllabus) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "更新课表信息失败，请联系管理人员！"));
		}
	}

	@Override
	public SyllabusView getSyllabus(String syllabus_id) {
		return syllabusDao.getSyllabus(syllabus_id);
	}

	@Override
	public void loadCsv(String fileName, String path, MultipartFile file) {
		String filePath = path + "/" + fileName;
		CsvReader cr = null;
		CsvWriter cw = null;
		try {
			cr = new CsvReader(file.getInputStream(), Charset.forName("GBK"));
			cw = new CsvWriter(filePath);

			while (cr.readRecord()) {
				String[] values = cr.getValues();
				String[] targetValues = new String[5];
				targetValues[0] = RandomID.getID() + "";
				for (int i = 0; i < values.length; i++) {
					targetValues[i + 1] = values[i];
				}
				cw.writeRecord(targetValues);
			}
		} catch (IOException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.FILE_IO_ERROR);
		} finally {
			if (cr != null) {
				cr.close();
			}
			if (cw != null) {
				cw.close();
			}
		}
		syllabusDao.loadCsv(filePath);
		File target = new File(filePath);
		if (target.exists()) {
			target.delete();
		}
	}

	@Override
	public void deleteData(List<String> syllabus_id) {
		syllabusDao.deleteData(syllabus_id);
	}

}
