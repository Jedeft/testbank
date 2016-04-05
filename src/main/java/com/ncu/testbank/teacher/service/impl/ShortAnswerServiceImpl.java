package com.ncu.testbank.teacher.service.impl;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.ncu.testbank.base.utils.FileUpload;
import com.ncu.testbank.base.utils.PathUtil;
import com.ncu.testbank.base.utils.QiniuImgUtil;
import com.ncu.testbank.base.utils.RandomID;
import com.ncu.testbank.base.utils.UuidUtil;
import com.ncu.testbank.permission.data.User;
import com.ncu.testbank.teacher.dao.IShortAnswerDao;
import com.ncu.testbank.teacher.dao.IShortAnswerExamDao;
import com.ncu.testbank.teacher.data.ShortAnswer;
import com.ncu.testbank.teacher.data.params.DELQuestionParams;
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
				String questionUrl = shortAnswer.getQuestion();
				String answerUrl = shortAnswer.getAnswer();
				String questionKey = questionUrl.substring(questionUrl
						.lastIndexOf("/") + 1);
				String answerKey = answerUrl.substring(answerUrl
						.lastIndexOf("/") + 1);
				try {
					QiniuImgUtil.delete("testBank", questionKey);
					QiniuImgUtil.delete("testBank", answerKey);
				} catch (IOException e) {
					log.error(e);
					throw new ServiceException(ErrorCode.FILE_IO_ERROR);
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

		Properties properties = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("qiniu.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.FILE_PROPERTIES_ERROR);
		}
		String filePath = PathUtil.getClasspath() + "uploadFiles/uploadImgs";
		String qusetionFileName = FileUpload.fileUp(questionFile, filePath,
				UuidUtil.get32UUID()); // 执行上传
		String answerFileName = FileUpload.fileUp(answerFile, filePath,
				UuidUtil.get32UUID()); // 执行上传
		String url = properties.getProperty("url");
		File questUploadFile = new File(filePath + "/" + qusetionFileName);
		File answerUploadFile = new File(filePath + "/" + qusetionFileName);
		if (!questUploadFile.exists()) {
			throw new ServiceException(ErrorCode.FILE_IO_ERROR);
		}
		if (!answerUploadFile.exists()) {
			throw new ServiceException(ErrorCode.FILE_IO_ERROR);
		}
		String questionKey = null;
		String answerKey = null;
		try {
			questionKey = QiniuImgUtil.uploadImg("testbank", qusetionFileName,
					questUploadFile);
			answerKey = QiniuImgUtil.uploadImg("testbank", answerFileName,
					answerUploadFile);
		} catch (IOException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.FILE_IO_ERROR);
		}

		shortAnswer.setQuestion("http://" + url + "/" + questionKey);
		shortAnswer.setAnswer("http://" + url + "/" + answerKey);
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
		Properties properties = new Properties();
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("qiniu.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.FILE_PROPERTIES_ERROR);
		}
		String filePath = PathUtil.getClasspath() + "uploadFiles/uploadImgs";

		if (questionFile != null) {
			String questionUrl = shortAnswer.getQuestion();
			String deleteKey = questionUrl.substring(questionUrl
					.lastIndexOf("/") + 1);
			try {
				QiniuImgUtil.delete("testbank", deleteKey);
			} catch (IOException e) {
				log.error(e);
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
			String fileName = FileUpload.fileUp(questionFile, filePath,
					UuidUtil.get32UUID()); // 执行上传
			String url = properties.getProperty("url");
			File uploadFile = new File(filePath + "/" + fileName);
			if (!uploadFile.exists()) {
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
			String key = null;
			try {
				key = QiniuImgUtil.uploadImg("testbank", fileName, uploadFile);
			} catch (IOException e) {
				log.error(e);
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
			shortAnswer.setQuestion("http://" + url + "/" + key);
		}

		if (answerFile != null) {
			String answerUrl = shortAnswer.getQuestion();
			String deleteKey = answerUrl
					.substring(answerUrl.lastIndexOf("/") + 1);
			try {
				QiniuImgUtil.delete("testbank", deleteKey);
			} catch (IOException e) {
				log.error(e);
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
			String fileName = FileUpload.fileUp(answerFile, filePath,
					UuidUtil.get32UUID()); // 执行上传
			String url = properties.getProperty("url");
			File uploadFile = new File(filePath + "/" + fileName);
			if (!uploadFile.exists()) {
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
			String key = null;
			try {
				key = QiniuImgUtil.uploadImg("testbank", fileName, uploadFile);
			} catch (IOException e) {
				log.error(e);
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
			shortAnswer.setAnswer("http://" + url + "/" + key);
		}
		shortAnswerDao.updateOne(shortAnswer);
	}

	@Override
	public void updateExamStuAnswer(Long exam_id, Long question_id, String answer) {
		Map<String, Object> params = new HashMap<>();
		params.put("answer", answer);
		params.put("exam_id", exam_id);
		params.put("question_id", question_id);
		shortAnswerExamDao.updateStuAnswer(params);
	}
}
