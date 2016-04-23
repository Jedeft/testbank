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
import com.ncu.testbank.student.dao.IMultiplePractiseDao;
import com.ncu.testbank.teacher.dao.IMultipleDao;
import com.ncu.testbank.teacher.dao.IMultipleExamDao;
import com.ncu.testbank.teacher.data.Multiple;
import com.ncu.testbank.teacher.data.params.DELQuestionParams;
import com.ncu.testbank.teacher.data.view.MultipleView;
import com.ncu.testbank.teacher.service.IMultipleService;

@Service("multipleService")
public class MultipleServiceImpl implements IMultipleService {

	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private IMultipleDao multipleDao;

	@Autowired
	private IMultipleExamDao multipleExamDao;

	@Autowired
	private IMultiplePractiseDao multiplePractiseDao;

	@Override
	public void insertWriting(Multiple multiple, User user) {
		multiple.setQuestion_id(RandomID.getID());
		multiple.setType(1);
		multiple.setCreate_time(new Timestamp(new Date().getTime()));
		multiple.setCreate_teacher_id(user.getUsername());

		if (multipleDao.insertOne(multiple) < 1) {
			throw new ServiceException(new ErrorCode(30001, "多选题添加失败，请联系管理人员！"));
		}
	}

	@Override
	public void updateWriting(Multiple multiple, User user) {
		multiple.setModify_time(new Timestamp(new Date().getTime()));
		multiple.setModify_teacher_id(user.getUsername());

		if (multipleDao.updateOne(multiple) < 1) {
			throw new ServiceException(new ErrorCode(30001, "多选题修改失败，请联系管理人员！"));
		}
	}

	@Override
	public List<MultipleView> searchData(PageInfo page, Multiple multiple) {
		Map<String, Object> params = null;
		try {
			params = BeanToMapUtils.convertBean(multiple);
		} catch (IllegalAccessException | InvocationTargetException
				| IntrospectionException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.MAP_CONVERT_ERROR);
		}
		int count = multipleDao.getCount(params);
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
		return multipleDao.searchData(params);
	}

	@Override
	public Multiple getMultiple(long question_id) {
		return multipleDao.getOne(question_id);
	}

	@Override
	public void deleteQuestion(List<DELQuestionParams> list) {
		List<Long> question_id = new ArrayList<>();
		for (DELQuestionParams question : list) {
			if (question.getType() == 2) {
				// 图片题目处理
				Multiple multiple = multipleDao.getOne(question
						.getQuestion_id());
				String questionUrl = multiple.getQuestion();
				String key = questionUrl
						.substring(questionUrl.lastIndexOf("/") + 1);
				try {
					QiniuImgUtil.delete("testBank", key);
				} catch (IOException e) {
					log.error(e);
					throw new ServiceException(ErrorCode.FILE_IO_ERROR);
				}
			}
			question_id.add(question.getQuestion_id());
		}
		multipleDao.deleteData(question_id);
	}

	@Override
	public void insertImge(Multiple multiple, User user, MultipartFile file) {
		multiple.setQuestion_id(RandomID.getID());
		multiple.setType(2);
		multiple.setCreate_teacher_id(user.getUsername());
		multiple.setCreate_time(new Timestamp(new Date().getTime()));

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
		String fileName = FileUpload.fileUp(file, filePath,
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

		multiple.setQuestion("http://" + url + "/" + key);
		multipleDao.insertOne(multiple);
	}

	@Override
	public void updateImge(Multiple multiple, User user, MultipartFile file) {
		multiple.setModify_teacher_id(user.getUsername());
		multiple.setModify_time(new Timestamp(new Date().getTime()));
		multiple.setType(2);

		Multiple dbMultiple = multipleDao.getOne(multiple.getQuestion_id());
		if (dbMultiple == null) {
			throw new ServiceException(new ErrorCode(30001,
					"要修改的题目不存在，请联系管理人员！"));
		}
		if (file != null) {
			Properties properties = new Properties();
			InputStream in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("qiniu.properties");
			try {
				properties.load(in);
			} catch (IOException e) {
				log.error(e);
				throw new ServiceException(ErrorCode.FILE_PROPERTIES_ERROR);
			}
			String questionUrl = multiple.getQuestion();
			String deleteKey = questionUrl.substring(questionUrl
					.lastIndexOf("/") + 1);
			try {
				QiniuImgUtil.delete("testbank", deleteKey);
			} catch (IOException e) {
				log.error(e);
				throw new ServiceException(ErrorCode.FILE_IO_ERROR);
			}
			String filePath = PathUtil.getClasspath()
					+ "uploadFiles/uploadImgs";
			String fileName = FileUpload.fileUp(file, filePath,
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

			multiple.setQuestion("http://" + url + "/" + key);
		}
		multipleDao.updateOne(multiple);
	}

	@Override
	public void updateExamStuAnswer(Long exam_id, Long question_id,
			String answer) {
		Map<String, Object> params = new HashMap<>();
		params.put("answer", answer);
		params.put("exam_id", exam_id);
		params.put("question_id", question_id);
		multipleExamDao.updateStuAnswer(params);
	}

	@Override
	public void updatePractiseStuAnswer(Long practise_id, Long question_id,
			String answer) {
		Map<String, Object> params = new HashMap<>();
		params.put("answer", answer);
		params.put("practise_id", practise_id);
		params.put("question_id", question_id);
		multiplePractiseDao.updateStuAnswer(params);
	}
}
