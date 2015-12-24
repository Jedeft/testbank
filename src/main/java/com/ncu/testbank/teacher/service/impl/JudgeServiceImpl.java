package com.ncu.testbank.teacher.service.impl;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.response.PageInfo;
import com.ncu.testbank.base.utils.BeanToMapUtils;
import com.ncu.testbank.base.utils.RandomID;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.dao.IJudgeDao;
import com.ncu.testbank.teacher.data.Judge;
import com.ncu.testbank.teacher.data.params.DELQuestionParams;
import com.ncu.testbank.teacher.data.view.JudgeView;
import com.ncu.testbank.teacher.service.IJudgeService;

@Service("judgeService")
public class JudgeServiceImpl implements IJudgeService {

	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private IJudgeDao judgeDao;

	@Override
	public void insertWriting(Judge judge, User user) {
		judge.setQuestion_id(RandomID.getID());
		judge.setType(1);
		judge.setCreate_time(new Timestamp(new Date().getTime()));
		judge.setCreate_teacher_id(user.getUsername());

		if (judgeDao.insertOne(judge) < 1) {
			throw new ServiceException(new ErrorCode(30001, "单选题添加失败，请联系管理人员！"));
		}
	}

	@Override
	public void updateWriting(Judge judge, User user) {
		judge.setModify_time(new Timestamp(new Date().getTime()));
		judge.setModify_teacher_id(user.getUsername());

		if (judgeDao.updateOne(judge) < 1) {
			throw new ServiceException(new ErrorCode(30001, "单选题修改失败，请联系管理人员！"));
		}
	}

	@Override
	public List<JudgeView> searchData(PageInfo page, Judge judge) {
		Map<String, Object> params = null;
		try {
			params = BeanToMapUtils.convertBean(judge);
		} catch (IllegalAccessException | InvocationTargetException
				| IntrospectionException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.MAP_CONVERT_ERROR);
		}
		int count = judgeDao.getCount(params);
		page.setTotal(count);
		if (page.getRows() == 0) {
			throw new ServiceException(new ErrorCode(30001, "分页信息错误，请联系管理人员！"));
		}
		page.setTotalPage(count / page.getRows() + 1);
		if (count <= 0) {
			throw new ServiceException(new ErrorCode(30001, "没有符合查询条件的题目！"));
		}
		// 数据库分页从0开始，前台分页从1开始
		params.put("page", page.getPage() - 1);
		params.put("rows", page.getRows());
		return judgeDao.searchData(params);
	}

	@Override
	public Judge getJudge(long question_id) {
		return judgeDao.getOne(question_id);
	}

	@Override
	public void deleteQuestion(List<DELQuestionParams> list) {
		List<Long> question_id = new ArrayList<>();
		for (DELQuestionParams question : list) {
			if (question.getType() == 2) {
				// 图片题目处理
				Judge judge = judgeDao.getOne(question.getQuestion_id());
				String fileName = judge.getQuestion();
				File file = new File(fileName);
				if (file.exists()) {
					file.delete();
				}
			}
			question_id.add(question.getQuestion_id());
		}
		judgeDao.deleteData(question_id);
	}

	@Override
	public void insertImge(Judge judge, User user, MultipartFile file) {
		judge.setQuestion_id(RandomID.getID());
		judge.setType(2);
		judge.setCreate_teacher_id(user.getUsername());
		judge.setCreate_time(new Timestamp(new Date().getTime()));

		Properties fileConfig = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("questionImg.properties");
		try {
			fileConfig.load(in);
		} catch (IOException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.FILE_PROPERTIES_ERROR);
		}
		String filePath = (String) fileConfig.get("judgePath");
		String fileName = file.getOriginalFilename();
		int suffix = fileName.lastIndexOf(".");
		String targetName = judge.getQuestion_id()
				+ fileName.substring(suffix, fileName.length());
		File target = new File(filePath, targetName);

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

		judge.setQuestion(filePath + "/" + targetName);
		judgeDao.insertOne(judge);
	}

	@Override
	public void updateImge(Judge judge, User user, MultipartFile file) {
		judge.setModify_teacher_id(user.getUsername());
		judge.setModify_time(new Timestamp(new Date().getTime()));
		judge.setType(2);

		Judge dbjudge = judgeDao.getOne(judge.getQuestion_id());
		if (dbjudge == null) {
			throw new ServiceException(new ErrorCode(30001,
					"要修改的题目不存在，请联系管理人员！"));
		}
		if (file != null) {
			File serverFile = new File(dbjudge.getQuestion());
			if (serverFile.exists()) {
				serverFile.delete();
			}

			Properties fileConfig = new Properties();
			InputStream in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("questionImg.properties");
			try {
				fileConfig.load(in);
			} catch (IOException e) {
				log.error(e);
				throw new ServiceException(ErrorCode.FILE_PROPERTIES_ERROR);
			}
			String filePath = (String) fileConfig.get("judgePath");
			String fileName = file.getOriginalFilename();
			int suffix = fileName.lastIndexOf(".");
			String targetName = judge.getQuestion_id()
					+ fileName.substring(suffix, fileName.length());
			File target = new File(filePath, targetName);

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

			judge.setQuestion(filePath + "/" + targetName);
		}
		judgeDao.updateOne(judge);
	}

}
