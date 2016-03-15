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
import com.ncu.testbank.teacher.dao.IShortAnswerDao;
import com.ncu.testbank.teacher.dao.IShortAnswerExamDao;
import com.ncu.testbank.teacher.data.ShortAnswer;
import com.ncu.testbank.teacher.data.params.DELQuestionParams;
import com.ncu.testbank.teacher.data.view.ShortAnswerExamView;
import com.ncu.testbank.teacher.data.view.ShortAnswerView;
import com.ncu.testbank.teacher.service.IShortAnswerService;

@Service("shortAnswerService")
public class ShortAnswerServiceImpl implements IShortAnswerService {

	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private IShortAnswerDao shortAnswerDao;
	
	@Autowired
	private IShortAnswerExamDao shortAnswerExamDao;

	@Override
	public void insertWriting(ShortAnswer shortAnswer, User user) {
		shortAnswer.setQuestion_id(RandomID.getID());
		shortAnswer.setType(1);
		shortAnswer.setCreate_time(new Timestamp(new Date().getTime()));
		shortAnswer.setCreate_teacher_id(user.getUsername());

		if (shortAnswerDao.insertOne(shortAnswer) < 1) {
			throw new ServiceException(
					new ErrorCode(30001, "简答题题添加失败，请联系管理人员！"));
		}
	}

	@Override
	public void updateWriting(ShortAnswer shortAnswer, User user) {
		shortAnswer.setModify_time(new Timestamp(new Date().getTime()));
		shortAnswer.setModify_teacher_id(user.getUsername());

		if (shortAnswerDao.updateOne(shortAnswer) < 1) {
			throw new ServiceException(new ErrorCode(30001, "简答题修改失败，请联系管理人员！"));
		}
	}

	@Override
	public List<ShortAnswerView> searchData(PageInfo page,
			ShortAnswer shortAnswer) {
		Map<String, Object> params = null;
		try {
			params = BeanToMapUtils.convertBean(shortAnswer);
		} catch (IllegalAccessException | InvocationTargetException
				| IntrospectionException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.MAP_CONVERT_ERROR);
		}
		int count = shortAnswerDao.getCount(params);
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
		return shortAnswerDao.searchData(params);
	}

	@Override
	public ShortAnswer getShortAnswer(long question_id) {
		return shortAnswerDao.getOne(question_id);
	}

	@Override
	public void deleteQuestion(List<DELQuestionParams> list) {
		List<Long> question_id = new ArrayList<>();
		for (DELQuestionParams question : list) {
			if (question.getType() == 2) {
				// 图片题目处理
				ShortAnswer shortAnswer = shortAnswerDao.getOne(question
						.getQuestion_id());
				String questionFileName = shortAnswer.getQuestion();
				File file = new File(questionFileName);
				if (file.exists()) {
					file.delete();
				}
				String answerFileName = shortAnswer.getAnswer();
				file = new File(answerFileName);
				if (file.exists()) {
					file.delete();
				}
			}
			question_id.add(question.getQuestion_id());
		}
		shortAnswerDao.deleteData(question_id);
	}

	@Override
	public void insertImge(ShortAnswer shortAnswer, User user,
			MultipartFile questionFile, MultipartFile answerFile) {
		shortAnswer.setQuestion_id(RandomID.getID());
		shortAnswer.setType(2);
		shortAnswer.setCreate_teacher_id(user.getUsername());
		shortAnswer.setCreate_time(new Timestamp(new Date().getTime()));

		Properties fileConfig = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("questionImg.properties");
		try {
			fileConfig.load(in);
		} catch (IOException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.FILE_PROPERTIES_ERROR);
		}
		String filePath = (String) fileConfig.get("shortAnswerPath");
		String questionFileName = questionFile.getOriginalFilename();
		String answerFileName = answerFile.getOriginalFilename();
		int questionSuffix = questionFileName.lastIndexOf(".");
		int answerSuffix = answerFileName.lastIndexOf(".");
		questionFileName = "Q"
				+ shortAnswer.getQuestion_id()
				+ questionFileName.substring(questionSuffix,
						questionFileName.length());
		answerFileName = "A"
				+ shortAnswer.getQuestion_id()
				+ answerFileName.substring(answerSuffix,
						answerFileName.length());

		File questionTarget = new File(filePath, questionFileName);
		File answerTarget = new File(filePath, answerFileName);
		if (!questionTarget.getParentFile().exists()) {
			if (!questionTarget.getParentFile().mkdirs()) {
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
		}
		if (!answerTarget.getParentFile().exists()) {
			if (!answerTarget.getParentFile().mkdirs()) {
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
		}
		try {
			if (!questionTarget.createNewFile()) {
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
			questionFile.transferTo(questionTarget);
			if (!answerTarget.createNewFile()) {
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
			answerFile.transferTo(answerTarget);
		} catch (IOException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.FILE_IO_ERROR);
		}

		shortAnswer.setQuestion(filePath + "/" + questionFileName);
		shortAnswer.setAnswer(filePath + "/" + answerFileName);
		shortAnswerDao.insertOne(shortAnswer);
	}

	@Override
	public void updateImge(ShortAnswer shortAnswer, User user,
			MultipartFile questionFile, MultipartFile answerFile) {
		shortAnswer.setModify_teacher_id(user.getUsername());
		shortAnswer.setModify_time(new Timestamp(new Date().getTime()));
		shortAnswer.setType(2);

		ShortAnswer dbShortAnswer = shortAnswerDao.getOne(shortAnswer
				.getQuestion_id());
		if (dbShortAnswer == null) {
			throw new ServiceException(new ErrorCode(30001,
					"要修改的题目不存在，请联系管理人员！"));
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
		String filePath = (String) fileConfig.get("shortAnswerPath");

		if (questionFile != null) {
			File serverFile = new File(dbShortAnswer.getQuestion());
			if (serverFile.exists()) {
				serverFile.delete();
			}

			String fileName = questionFile.getOriginalFilename();
			int suffix = fileName.lastIndexOf(".");
			String targetName = "Q" + shortAnswer.getQuestion_id()
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
				questionFile.transferTo(target);
			} catch (IOException e) {
				log.error(e);
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}

			shortAnswer.setQuestion(filePath + "/" + targetName);
		}

		if (answerFile != null) {
			File serverFile = new File(dbShortAnswer.getAnswer());
			if (serverFile.exists()) {
				serverFile.delete();
			}

			String fileName = answerFile.getOriginalFilename();
			int suffix = fileName.lastIndexOf(".");
			String targetName = "A" + shortAnswer.getQuestion_id()
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
				answerFile.transferTo(target);
			} catch (IOException e) {
				log.error(e);
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}

			shortAnswer.setAnswer(filePath + "/" + targetName);
		}
		shortAnswerDao.updateOne(shortAnswer);
	}

	@Override
	public List<ShortAnswerExamView> searchExamShortNoAnswer(Long exam_id) {
		return shortAnswerExamDao.searchExamShortNoAnswer(exam_id);
	}

}
