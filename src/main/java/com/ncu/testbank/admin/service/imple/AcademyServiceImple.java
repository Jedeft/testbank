package com.ncu.testbank.admin.service.imple;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.admin.dao.IAcademyDao;
import com.ncu.testbank.admin.data.Academy;
import com.ncu.testbank.admin.service.IAcademyService;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.CsvReader;

@Service("academyService")
public class AcademyServiceImple implements IAcademyService {

	@Autowired
	private IAcademyDao academyDao;
	
	@Override
	public List<Academy> searchData(PageInfo page) {
		int count = academyDao.getCount();
		page.setTotal(count);
		page.setTotalPage(count/page.getRows());
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的学院！"));
		}
		return academyDao.searchData(page);
	}

	@Override
	public void insertOne(Academy academy) {
		if ( academyDao.insertOne(academy) < 1 ) {
			throw new ServiceException(new ErrorCode(30001, "添加学院信息失败，请重试！"));
		}
	}

	@Override
	public void deleteOne(String academy_id) {
		if ( academyDao.deleteOne(academy_id) < 1 ) {
			throw new ServiceException(new ErrorCode(30001, "删除学院信息失败，请重试！"));
		}
	}

	@Override
	public void updateOne(Academy academy) {
		if ( academyDao.updateOne(academy) < 1 ) {
			throw new ServiceException(new ErrorCode(30001, "更新学院信息失败，请重试！"));
		}
	}

	@Override
	public Academy getAcademy(String academy_id) {
		return academyDao.getAcademy(academy_id);
	}

	@Override
	public void loadCsv(String fileName, String path, MultipartFile file) throws IllegalStateException, IOException {
		File target = new File(path, fileName);
		if (!target.exists()) {
			if ( !target.mkdirs() ) {
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
		}
		
		file.transferTo(target);
		academyDao.loadCsv(target.getPath());
		if (target.exists()) {
			target.delete();
		}
	}

}
