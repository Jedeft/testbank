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
import com.ncu.testbank.student.dao.ISinglePractiseDao;
import com.ncu.testbank.teacher.dao.ISingleDao;
import com.ncu.testbank.teacher.dao.ISingleExamDao;
import com.ncu.testbank.teacher.data.Single;
import com.ncu.testbank.teacher.data.params.DELQuestionParams;
import com.ncu.testbank.teacher.data.view.SingleView;
import com.ncu.testbank.teacher.service.ISingleService;

@Service("singleService")
public class SingleServiceImpl implements ISingleService {

	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private ISingleDao singleDao;

	@Autowired
	private ISingleExamDao singleExamDao;

	@Autowired
	private ISinglePractiseDao singlePractiseDao;

	@Override
	public void insertWriting(Single single, User user) {
		single.setQuestion_id(RandomID.getID());
		single.setType(1);
		single.setCreate_time(new Timestamp(new Date().getTime()));
		single.setCreate_teacher_id(user.getUsername());

		if (singleDao.insertOne(single) < 1) {
			throw new ServiceException(new ErrorCode(30001, "单选题添加失败，请联系管理人员！"));
		}
	}

	@Override
	public void updateWriting(Single single, User user) {
		single.setModify_time(new Timestamp(new Date().getTime()));
		single.setModify_teacher_id(user.getUsername());

		if (singleDao.updateOne(single) < 1) {
			throw new ServiceException(new ErrorCode(30001, "单选题修改失败，请联系管理人员！"));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SingleView> searchData(PageInfo page, Single single) {
		Map<String, Object> params = null;
		try {
			params = BeanToMapUtils.convertBean(single);
		} catch (IllegalAccessException | InvocationTargetException
				| IntrospectionException e) {
			log.error(e);
			throw new ServiceException(ErrorCode.MAP_CONVERT_ERROR);
		}
		int count = singleDao.getCount(params);
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
		return singleDao.searchData(params);
	}

	@Override
	public Single getSingle(long question_id) {
		return singleDao.getOne(question_id);
	}

	@Override
	public void deleteQuestion(List<DELQuestionParams> list) {
		List<Long> question_id = new ArrayList<>();
		for (DELQuestionParams question : list) {
			if (question.getType() == 2) {
				// 图片题目处理
				Single single = singleDao.getOne(question.getQuestion_id());
				String questionUrl = single.getQuestion();
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
		singleDao.deleteData(question_id);
	}

	@Override
	public void insertImge(Single single, User user, MultipartFile file) {
		single.setQuestion_id(RandomID.getID());
		single.setType(2);
		single.setCreate_teacher_id(user.getUsername());
		single.setCreate_time(new Timestamp(new Date().getTime()));

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
		single.setQuestion("http://" + url + "/" + key);
		singleDao.insertOne(single);
	}

	@Override
	public void updateImge(Single single, User user, MultipartFile file) {
		single.setModify_teacher_id(user.getUsername());
		single.setModify_time(new Timestamp(new Date().getTime()));
		single.setType(2);

		Single dbSingle = singleDao.getOne(single.getQuestion_id());
		if (dbSingle == null) {
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
			String questionUrl = single.getQuestion();
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
			single.setQuestion("http://" + url + "/" + key);
		}
		singleDao.updateOne(single);
	}

	@Override
	public void updateExamStuAnswer(Long exam_id, Long question_id,
			String answer) {
		Map<String, Object> params = new HashMap<>();
		params.put("answer", answer);
		params.put("exam_id", exam_id);
		params.put("question_id", question_id);
		singleExamDao.updateStuAnswer(params);
	}

	@Override
	public void updatePractiseStuAnswer(Long practise_id, Long question_id,
			String answer) {
		Map<String, Object> params = new HashMap<>();
		params.put("answer", answer);
		params.put("practise_id", practise_id);
		params.put("question_id", question_id);
		singlePractiseDao.updateStuAnswer(params);
	}
}
