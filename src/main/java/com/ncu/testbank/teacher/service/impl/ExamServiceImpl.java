package com.ncu.testbank.teacher.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.ncu.testbank.admin.dao.ITeacherDao;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.utils.ExamUtils;
import com.ncu.testbank.base.utils.RandomID;
import com.ncu.testbank.base.utils.RandomUtils;
import com.ncu.testbank.teacher.dao.IExamDao;
import com.ncu.testbank.teacher.dao.IJudgeDao;
import com.ncu.testbank.teacher.dao.IJudgeExamDao;
import com.ncu.testbank.teacher.dao.IMultipleDao;
import com.ncu.testbank.teacher.dao.IMultipleExamDao;
import com.ncu.testbank.teacher.dao.IShortAnswerDao;
import com.ncu.testbank.teacher.dao.IShortAnswerExamDao;
import com.ncu.testbank.teacher.dao.ISingleDao;
import com.ncu.testbank.teacher.dao.ISingleExamDao;
import com.ncu.testbank.teacher.dao.ITemplateDao;
import com.ncu.testbank.teacher.data.Exam;
import com.ncu.testbank.teacher.data.Judge;
import com.ncu.testbank.teacher.data.Multiple;
import com.ncu.testbank.teacher.data.Question;
import com.ncu.testbank.teacher.data.QuestionCount;
import com.ncu.testbank.teacher.data.QuestionLevel;
import com.ncu.testbank.teacher.data.ShortAnswer;
import com.ncu.testbank.teacher.data.Single;
import com.ncu.testbank.teacher.data.Template;
import com.ncu.testbank.teacher.data.view.ExamPaperView;
import com.ncu.testbank.teacher.data.view.HistoryExamView;
import com.ncu.testbank.teacher.data.view.JudgeExamView;
import com.ncu.testbank.teacher.data.view.MultipleExamView;
import com.ncu.testbank.teacher.data.view.OnlineExamView;
import com.ncu.testbank.teacher.data.view.SingleExamView;
import com.ncu.testbank.teacher.service.IExamService;

@Service("examService")
public class ExamServiceImpl implements IExamService {

	private Logger log = Logger.getLogger("testbankLog");

	@Autowired
	private ITemplateDao templateDao;

	@Autowired
	private IExamDao examDao;

	@Autowired
	private ISingleDao singleDao;

	@Autowired
	private IMultipleDao multipleDao;

	@Autowired
	private IJudgeDao judgeDao;

	@Autowired
	private IShortAnswerDao shortAnswerDao;

	@Autowired
	private ITeacherDao teacherDao;

	@Autowired
	private ISingleExamDao singleExamDao;

	@Autowired
	private IMultipleExamDao multipleExamDao;

	@Autowired
	private IJudgeExamDao judgeExamDao;

	@Autowired
	private IShortAnswerExamDao shortAnswerExamDao;

	@Override
	public Exam createExam(Template template, String user_id, Timestamp start,
			Timestamp end) {
		Exam exam = new Exam(RandomID.getID(), template.getTemplate_id(),
				start, end, user_id);
		String pointStr = template.getPoint_id().replaceAll(" ", "");
		String[] pointStrArray = pointStr.split(",");
		List<String> tempList = Lists.newArrayList(pointStrArray);
		List<Long> points = new ArrayList<>();
		for (String s : tempList) {
			points.add(Long.valueOf(s));
		}

		if (examDao.insertOne(exam) < 1) {
			log.error(user_id + "使用者组卷失败！原因：插入exam试卷表出错！");
			if ("1".equals(template.getType())) {
				// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
				return null;
			}
			throw new ServiceException(ErrorCode.ERROR_CREATE_EXAM);
		}
		// 对应档次难度等级
		QuestionLevel questionLevel = ExamUtils.initLevel(template.getLevel());

		if (template.getSingle_num() > 0) {
			// 对应难度题目数量
			QuestionCount questionCount = ExamUtils.initCount(
					template.getSingle_num(), template);
			int hardCount = questionCount.getHardCount();
			int mediumCount = questionCount.getMediumCount();
			int easyCount = questionCount.getEasyCount();

			List<Question> list = singleDao.searchByCourse(template
					.getCourse_id());
			Map<Long, List<Question>> hardMap = new HashMap<>();
			Map<Long, List<Question>> mediumMap = new HashMap<>();
			Map<Long, List<Question>> easyMap = new HashMap<>();

			for (Question question : list) {
				question.setTest_id(exam.getExam_id());
				if (question.getLevel() == questionLevel.getHard()) {
					if (!hardMap.containsKey(question.getPoint_id())) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						hardMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = hardMap.get(question
								.getPoint_id());
						questions.add(question);
						hardMap.put(question.getPoint_id(), questions);
					}
				} else if (question.getLevel() == questionLevel.getMedium()) {
					if (!mediumMap.containsKey(question.getPoint_id())) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						mediumMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = mediumMap.get(question
								.getPoint_id());
						questions.add(question);
						mediumMap.put(question.getPoint_id(), questions);
					}
				} else if (question.getLevel() <= questionLevel.getEasy()) {
					if (!easyMap.containsKey(question.getPoint_id())) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						easyMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = easyMap.get(question
								.getPoint_id());
						questions.add(question);
						easyMap.put(question.getPoint_id(), questions);
					}
				}
			}

			List<Long> hardPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : hardMap.entrySet()) {
				if (points.contains(entry.getKey())) {
					hardPoints.add(entry.getKey());
				}
			}
			List<Long> mediumPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : mediumMap.entrySet()) {
				if (points.contains(entry.getKey())) {
					mediumPoints.add(entry.getKey());
				}
			}
			List<Long> easyPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : easyMap.entrySet()) {
				if (points.contains(entry.getKey())) {
					easyPoints.add(entry.getKey());
				}
			}

			// 最高难度题目组卷
			int total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : hardMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (hardCount > total) {
				mediumCount += (total - hardCount);
				hardCount = total;
			}
			if (!this.createSingle(hardCount, hardPoints, hardMap, exam)) {
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60003,
						"组卷失败！原因：题目录入single_exam表中失败。"));
			}
			// 次之难度题目组卷
			total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : mediumMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (mediumCount > total) {
				easyCount += (total - mediumCount);
				mediumCount = total;
			}
			if (!this.createSingle(mediumCount, mediumPoints, mediumMap, exam)) {
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60003,
						"组卷失败！原因：题目录入single_exam表中失败。"));
			}
			// 最低难度题目组卷
			total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : easyMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (easyCount > total) {
				log.error(user_id + "组卷失败！原因：题库题目数量过少，无法完成组卷！");
				examDao.deleteOne(exam.getExam_id());
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60002,
						"组卷失败！原因：题库题目数量过少，无法完成组卷！"));
			}
			if (!this.createSingle(easyCount, easyPoints, easyMap, exam)) {
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60003,
						"组卷失败！原因：题目录入single_exam表中失败。"));
			}
		}
		if (template.getMultiple_num() > 0) {
			// 对应难度题目数量
			QuestionCount questionCount = ExamUtils.initCount(
					template.getMultiple_num(), template);
			int hardCount = questionCount.getHardCount();
			int mediumCount = questionCount.getMediumCount();
			int easyCount = questionCount.getEasyCount();

			List<Question> list = multipleDao.searchByCourse(template
					.getCourse_id());
			Map<Long, List<Question>> hardMap = new HashMap<>();
			Map<Long, List<Question>> mediumMap = new HashMap<>();
			Map<Long, List<Question>> easyMap = new HashMap<>();

			for (Question question : list) {
				question.setTest_id(exam.getExam_id());
				if (question.getLevel() == questionLevel.getHard()) {
					if (!hardMap.containsKey(question.getPoint_id())) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						hardMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = hardMap.get(question
								.getPoint_id());
						questions.add(question);
						hardMap.put(question.getPoint_id(), questions);
					}
				} else if (question.getLevel() == questionLevel.getMedium()) {
					if (!mediumMap.containsKey(question.getPoint_id())) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						mediumMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = mediumMap.get(question
								.getPoint_id());
						questions.add(question);
						mediumMap.put(question.getPoint_id(), questions);
					}
				} else if (question.getLevel() <= questionLevel.getEasy()) {
					if (!easyMap.containsKey(question.getPoint_id())) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						easyMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = easyMap.get(question
								.getPoint_id());
						questions.add(question);
						easyMap.put(question.getPoint_id(), questions);
					}
				}
			}

			List<Long> hardPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : hardMap.entrySet()) {
				if (points.contains(entry.getKey())) {
					hardPoints.add(entry.getKey());
				}
			}
			List<Long> mediumPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : mediumMap.entrySet()) {
				if (points.contains(entry.getKey())) {
					mediumPoints.add(entry.getKey());
				}
			}
			List<Long> easyPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : easyMap.entrySet()) {
				if (points.contains(entry.getKey())) {
					easyPoints.add(entry.getKey());
				}
			}

			// 最高难度题目组卷
			int total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : hardMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (hardCount > total) {
				mediumCount += (total - hardCount);
				hardCount = total;
			}
			if (!this.createMultiple(hardCount, hardPoints, hardMap, exam)) {
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60005,
						"组卷失败！原因：题目录入multiple_exam表中失败。"));
			}
			// 次之难度题目组卷
			total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : mediumMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (mediumCount > total) {
				easyCount += (total - mediumCount);
				mediumCount = total;
			}
			if (!this
					.createMultiple(mediumCount, mediumPoints, mediumMap, exam)) {
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60005,
						"组卷失败！原因：题目录入multiple_exam表中失败。"));
			}
			// 最低难度题目组卷
			total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : easyMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (easyCount > total) {
				log.error(user_id + "组卷失败！原因：题库题目数量过少，无法完成组卷！");
				examDao.deleteOne(exam.getExam_id());
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60002,
						"组卷失败！原因：题库题目数量过少，无法完成组卷！"));
			}
			if (!this.createMultiple(easyCount, easyPoints, easyMap, exam)) {
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60005,
						"组卷失败！原因：题目录入multiple_exam表中失败。"));
			}
		}
		if (template.getJudge_num() > 0) {
			// 对应难度题目数量
			QuestionCount questionCount = ExamUtils.initCount(
					template.getJudge_num(), template);
			int hardCount = questionCount.getHardCount();
			int mediumCount = questionCount.getMediumCount();
			int easyCount = questionCount.getEasyCount();

			List<Question> list = judgeDao.searchByCourse(template
					.getCourse_id());
			Map<Long, List<Question>> hardMap = new HashMap<>();
			Map<Long, List<Question>> mediumMap = new HashMap<>();
			Map<Long, List<Question>> easyMap = new HashMap<>();

			for (Question question : list) {
				question.setTest_id(exam.getExam_id());
				if (question.getLevel() == questionLevel.getHard()) {
					if (!hardMap.containsKey(question.getPoint_id())) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						hardMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = hardMap.get(question
								.getPoint_id());
						questions.add(question);
						hardMap.put(question.getPoint_id(), questions);
					}
				} else if (question.getLevel() == questionLevel.getMedium()) {
					if (!mediumMap.containsKey(question.getPoint_id())) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						mediumMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = mediumMap.get(question
								.getPoint_id());
						questions.add(question);
						mediumMap.put(question.getPoint_id(), questions);
					}
				} else if (question.getLevel() <= questionLevel.getEasy()) {
					if (!easyMap.containsKey(question.getPoint_id())) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						easyMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = easyMap.get(question
								.getPoint_id());
						questions.add(question);
						easyMap.put(question.getPoint_id(), questions);
					}
				}
			}

			List<Long> hardPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : hardMap.entrySet()) {
				if (points.contains(entry.getKey())) {
					hardPoints.add(entry.getKey());
				}
			}
			List<Long> mediumPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : mediumMap.entrySet()) {
				if (points.contains(entry.getKey())) {
					mediumPoints.add(entry.getKey());
				}
			}
			List<Long> easyPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : easyMap.entrySet()) {
				if (points.contains(entry.getKey())) {
					easyPoints.add(entry.getKey());
				}
			}

			// 最高难度题目组卷
			int total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : hardMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (hardCount > total) {
				mediumCount += (total - hardCount);
				hardCount = total;
			}
			if (!this.createJudge(hardCount, hardPoints, hardMap, exam)) {
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60006,
						"组卷失败！原因：题目录入judge_exam表中失败。"));
			}
			// 次之难度题目组卷
			total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : mediumMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (mediumCount > total) {
				easyCount += (total - mediumCount);
				mediumCount = total;
			}
			if (!this.createJudge(mediumCount, mediumPoints, mediumMap, exam)) {
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60006,
						"组卷失败！原因：题目录入judge_exam表中失败。"));
			}
			// 最低难度题目组卷
			total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : easyMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (easyCount > total) {
				log.error(user_id + "组卷失败！原因：题库题目数量过少，无法完成组卷！");
				examDao.deleteOne(exam.getExam_id());
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60002,
						"组卷失败！原因：题库题目数量过少，无法完成组卷！"));
			}
			if (!this.createJudge(easyCount, easyPoints, easyMap, exam)) {
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60006,
						"组卷失败！原因：题目录入judge_exam表中失败。"));
			}
		}
		if (template.getShortAnswer_num() > 0) {
			// 对应难度题目数量
			QuestionCount questionCount = ExamUtils.initCount(
					template.getShortAnswer_num(), template);
			int hardCount = questionCount.getHardCount();
			int mediumCount = questionCount.getMediumCount();
			int easyCount = questionCount.getEasyCount();

			List<Question> list = shortAnswerDao.searchByCourse(template
					.getCourse_id());
			Map<Long, List<Question>> hardMap = new HashMap<>();
			Map<Long, List<Question>> mediumMap = new HashMap<>();
			Map<Long, List<Question>> easyMap = new HashMap<>();

			for (Question question : list) {
				question.setTest_id(exam.getExam_id());
				if (question.getLevel() == questionLevel.getHard()) {
					if (!hardMap.containsKey(question.getPoint_id())) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						hardMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = hardMap.get(question
								.getPoint_id());
						questions.add(question);
						hardMap.put(question.getPoint_id(), questions);
					}
				} else if (question.getLevel() == questionLevel.getMedium()) {
					if (!mediumMap.containsKey(question.getPoint_id())) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						mediumMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = mediumMap.get(question
								.getPoint_id());
						questions.add(question);
						mediumMap.put(question.getPoint_id(), questions);
					}
				} else if (question.getLevel() <= questionLevel.getEasy()) {
					if (!easyMap.containsKey(question.getPoint_id())) {
						List<Question> questions = new ArrayList<>();
						questions.add(question);
						easyMap.put(question.getPoint_id(), questions);
					} else {
						List<Question> questions = easyMap.get(question
								.getPoint_id());
						questions.add(question);
						easyMap.put(question.getPoint_id(), questions);
					}
				}
			}

			List<Long> hardPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : hardMap.entrySet()) {
				if (points.contains(entry.getKey())) {
					hardPoints.add(entry.getKey());
				}
			}
			List<Long> mediumPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : mediumMap.entrySet()) {
				if (points.contains(entry.getKey())) {
					mediumPoints.add(entry.getKey());
				}
			}
			List<Long> easyPoints = new ArrayList<>();
			for (Entry<Long, List<Question>> entry : easyMap.entrySet()) {
				if (points.contains(entry.getKey())) {
					easyPoints.add(entry.getKey());
				}
			}

			// 最高难度题目组卷
			int total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : hardMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (hardCount > total) {
				mediumCount += (total - hardCount);
				hardCount = total;
			}
			if (!this.createShortAnswer(hardCount, hardPoints, hardMap, exam)) {
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60007,
						"组卷失败！原因：题目录入shortAnswer_exam表中失败。"));
			}
			// 次之难度题目组卷
			total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : mediumMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (mediumCount > total) {
				easyCount += (total - mediumCount);
				mediumCount = total;
			}
			if (!this.createShortAnswer(mediumCount, mediumPoints, mediumMap,
					exam)) {
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60007,
						"组卷失败！原因：题目录入shortAnswer_exam表中失败。"));
			}
			// 最低难度题目组卷
			total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : easyMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (easyCount > total) {
				log.error(user_id + "组卷失败！原因：题库题目数量过少，无法完成组卷！");
				examDao.deleteOne(exam.getExam_id());
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60002,
						"组卷失败！原因：题库题目数量过少，无法完成组卷！"));
			}
			if (!this.createShortAnswer(easyCount, easyPoints, easyMap, exam)) {
				if ("1".equals(template.getType())) {
					// 若为在线考试，出现组卷失败，那么直接返回null。在后台程序处理后统一邮件通知
					return null;
				}
				throw new ServiceException(new ErrorCode(60007,
						"组卷失败！原因：题目录入shortAnswer_exam表中失败。"));
			}
		}

		return exam;
	}

	private boolean createSingle(int questionCount, List<Long> points,
			Map<Long, List<Question>> map, Exam exam) {
		if (points.size() == 0) {
			return false;
		}
		if (questionCount == 1) {
			// 只有一道题目时，随机出题
			int random = RandomUtils.random(0, points.size() - 1);
			long point_id = points.get(random);
			List<Question> questions = map.get(point_id);
			Question question = questions.get(RandomUtils.random(0,
					questions.size() - 1));
			if (singleExamDao.insertSingle(question) < 1) {
				log.error(exam.getUser_id() + "组卷失败！原因：题目录入single_exam表中失败。");
				examDao.deleteOne(exam.getExam_id());
				return false;
			}
		} else if (questionCount > 1 && questionCount <= points.size()) {
			// 出题数量大于1，但是小于知识点数量，保证每个题目考点不同
			for (int i = 0; i < questionCount; i++) {
				List<Question> questions = map.get(points.get(i));
				Question question = questions.get(RandomUtils.random(0,
						questions.size() - 1));
				if (singleExamDao.insertSingle(question) < 1) {
					log.error(exam.getUser_id()
							+ "组卷失败！原因：题目录入single_exam表中失败。");
					examDao.deleteOne(exam.getExam_id());
					return false;
				}
			}
		} else if (questionCount > points.size()) {
			// 出题数量大于题目考点数量，需要检测是否有重题，反复组卷。
			int flag = 0;
			// 总体全部组卷三次需要的大循环次数
			int max = (questionCount / points.size() + 1) * 3;
			// 已经添加的题目数量
			int count = 0;
			List<Long> question_id = new ArrayList<>();
			outer: while (flag < max && count < questionCount) {
				// 循环出卷
				for (int i = 0; i < points.size(); i++) {
					List<Question> questions = map.get(points.get(i));
					Question question = questions.get(RandomUtils.random(0,
							questions.size() - 1));
					if (count == 0) {
						if (singleExamDao.insertSingle(question) < 1) {
							log.error(exam.getUser_id()
									+ "组卷失败！原因：题目录入single_exam表中失败。");
							examDao.deleteOne(exam.getExam_id());
							return false;
						}
						question_id.add(question.getQuestion_id());
						count++;
					} else {
						// 查找已经完成组卷的题目是否出现重复
						if (!question_id.contains(question.getQuestion_id())) {
							if (singleExamDao.insertSingle(question) < 1) {
								log.error(exam.getUser_id()
										+ "组卷失败！原因：题目录入single_exam表中失败。");
								examDao.deleteOne(exam.getExam_id());
								return false;
							}
							question_id.add(question.getQuestion_id());
							count++;
						}
					}
					if (count == questionCount) {
						break outer;
					}
				}
				flag++;
			}
			if (flag >= max) {
				log.error(exam.getUser_id()
						+ "组卷失败！原因：可能由于题库题目数量太少，导致重组试卷超过3次以上！");
				examDao.deleteOne(exam.getExam_id());
				return false;
			}
		}
		return true;
	}

	private boolean createMultiple(int questionCount, List<Long> points,
			Map<Long, List<Question>> map, Exam exam) {
		if (points.size() == 0) {
			return false;
		}
		if (questionCount == 1) {
			// 只有一道题目时，随机出题
			int random = RandomUtils.random(0, points.size() - 1);
			long point_id = points.get(random);
			List<Question> questions = map.get(point_id);
			Question question = questions.get(RandomUtils.random(0,
					questions.size() - 1));
			if (multipleExamDao.insertMultiple(question) < 1) {
				log.error(exam.getUser_id() + "组卷失败！原因：题目录入multiple_exam表中失败。");
				examDao.deleteOne(exam.getExam_id());
				return false;
			}
		} else if (questionCount > 1 && questionCount <= points.size()) {
			// 出题数量大于1，但是小于知识点数量，保证每个题目考点不同
			for (int i = 0; i < questionCount; i++) {
				List<Question> questions = map.get(points.get(i));
				Question question = questions.get(RandomUtils.random(0,
						questions.size() - 1));
				if (multipleExamDao.insertMultiple(question) < 1) {
					log.error(exam.getUser_id()
							+ "组卷失败！原因：题目录入multiple_exam表中失败。");
					examDao.deleteOne(exam.getExam_id());
					return false;
				}
			}
		} else if (questionCount > points.size()) {
			// 出题数量大于题目考点数量，需要检测是否有重题，反复组卷。
			int flag = 0;
			// 总体全部组卷三次需要的大循环次数
			int max = (questionCount / points.size() + 1) * 3;
			// 已经添加的题目数量
			int count = 0;
			List<Long> question_id = new ArrayList<>();
			outer: while (flag < max && count < questionCount) {
				// 循环出卷
				for (int i = 0; i < points.size(); i++) {
					List<Question> questions = map.get(points.get(i));
					Question question = questions.get(RandomUtils.random(0,
							questions.size() - 1));
					if (count == 0) {
						if (multipleExamDao.insertMultiple(question) < 1) {
							log.error(exam.getUser_id()
									+ "组卷失败！原因：题目录入multiple_exam表中失败。");
							examDao.deleteOne(exam.getExam_id());
							return false;
						}
						question_id.add(question.getQuestion_id());
						count++;
					} else {
						// 查找已经完成组卷的题目是否出现重复
						if (!question_id.contains(question.getQuestion_id())) {
							if (multipleExamDao.insertMultiple(question) < 1) {
								log.error(exam.getUser_id()
										+ "组卷失败！原因：题目录入multiple_exam表中失败。");
								examDao.deleteOne(exam.getExam_id());
								return false;
							}
							question_id.add(question.getQuestion_id());
							count++;
						}
					}
					if (count == questionCount) {
						break outer;
					}
				}
				flag++;
			}
			if (flag >= max) {
				log.error(exam.getUser_id()
						+ "组卷失败！原因：可能由于题库题目数量太少，导致重组试卷超过3次以上！");
				examDao.deleteOne(exam.getExam_id());
				return false;
			}
		}
		return true;
	}

	private boolean createJudge(int questionCount, List<Long> points,
			Map<Long, List<Question>> map, Exam exam) {
		if (points.size() == 0) {
			return false;
		}
		if (questionCount == 1) {
			// 只有一道题目时，随机出题
			int random = RandomUtils.random(0, points.size() - 1);
			long point_id = points.get(random);
			List<Question> questions = map.get(point_id);
			Question question = questions.get(RandomUtils.random(0,
					questions.size() - 1));
			if (judgeExamDao.insertJudge(question) < 1) {
				log.error(exam.getUser_id() + "组卷失败！原因：题目录入judge_exam表中失败。");
				examDao.deleteOne(exam.getExam_id());
				return false;
			}
		} else if (questionCount > 1 && questionCount <= points.size()) {
			// 出题数量大于1，但是小于知识点数量，保证每个题目考点不同
			for (int i = 0; i < questionCount; i++) {
				List<Question> questions = map.get(points.get(i));
				Question question = questions.get(RandomUtils.random(0,
						questions.size() - 1));
				if (judgeExamDao.insertJudge(question) < 1) {
					log.error(exam.getUser_id() + "组卷失败！原因：题目录入judge_exam表中失败。");
					examDao.deleteOne(exam.getExam_id());
					return false;
				}
			}
		} else if (questionCount > points.size()) {
			// 出题数量大于题目考点数量，需要检测是否有重题，反复组卷。
			int flag = 0;
			// 总体全部组卷三次需要的大循环次数
			int max = (questionCount / points.size() + 1) * 3;
			// 已经添加的题目数量
			int count = 0;
			List<Long> question_id = new ArrayList<>();
			outer: while (flag < max && count < questionCount) {
				// 循环出卷
				for (int i = 0; i < points.size(); i++) {
					List<Question> questions = map.get(points.get(i));
					Question question = questions.get(RandomUtils.random(0,
							questions.size() - 1));
					if (count == 0) {
						if (judgeExamDao.insertJudge(question) < 1) {
							log.error(exam.getUser_id()
									+ "组卷失败！原因：题目录入judge_exam表中失败。");
							examDao.deleteOne(exam.getExam_id());
							return false;
						}
						question_id.add(question.getQuestion_id());
						count++;
					} else {
						// 查找已经完成组卷的题目是否出现重复
						if (!question_id.contains(question.getQuestion_id())) {
							if (judgeExamDao.insertJudge(question) < 1) {
								log.error(exam.getUser_id()
										+ "组卷失败！原因：题目录入judge_exam表中失败。");
								examDao.deleteOne(exam.getExam_id());
								return false;
							}
							question_id.add(question.getQuestion_id());
							count++;
						}
					}
					if (count == questionCount) {
						break outer;
					}
				}
				flag++;
			}
			if (flag >= max) {
				log.error(exam.getUser_id()
						+ "组卷失败！原因：可能由于题库题目数量太少，导致重组试卷超过3次以上！");
				examDao.deleteOne(exam.getExam_id());
				return false;
			}
		}
		return true;
	}

	private boolean createShortAnswer(int questionCount, List<Long> points,
			Map<Long, List<Question>> map, Exam exam) {
		if (points.size() == 0) {
			return false;
		}
		if (questionCount == 1) {
			// 只有一道题目时，随机出题
			int random = RandomUtils.random(0, points.size() - 1);
			long point_id = points.get(random);
			List<Question> questions = map.get(point_id);
			Question question = questions.get(RandomUtils.random(0,
					questions.size() - 1));
			if (shortAnswerExamDao.insertShortAnswer(question) < 1) {
				log.error(exam.getUser_id()
						+ "组卷失败！原因：题目录入shortAnswer_exam表中失败。");
				examDao.deleteOne(exam.getExam_id());
				return false;
			}
		} else if (questionCount > 1 && questionCount <= points.size()) {
			// 出题数量大于1，但是小于知识点数量，保证每个题目考点不同
			for (int i = 0; i < questionCount; i++) {
				List<Question> questions = map.get(points.get(i));
				Question question = questions.get(RandomUtils.random(0,
						questions.size() - 1));
				if (shortAnswerExamDao.insertShortAnswer(question) < 1) {
					log.error(exam.getUser_id()
							+ "组卷失败！原因：题目录入shortAnswer_exam表中失败。");
					examDao.deleteOne(exam.getExam_id());
					return false;
				}
			}
		} else if (questionCount > points.size()) {
			// 出题数量大于题目考点数量，需要检测是否有重题，反复组卷。
			int flag = 0;
			// 总体全部组卷三次需要的大循环次数
			int max = (questionCount / points.size() + 1) * 3;
			// 已经添加的题目数量
			int count = 0;
			List<Long> question_id = new ArrayList<>();
			outer: while (flag < max && count < questionCount) {
				// 循环出卷
				for (int i = 0; i < points.size(); i++) {
					List<Question> questions = map.get(points.get(i));
					Question question = questions.get(RandomUtils.random(0,
							questions.size() - 1));
					if (count == 0) {
						if (shortAnswerExamDao.insertShortAnswer(question) < 1) {
							log.error(exam.getUser_id()
									+ "组卷失败！原因：题目录入shortAnswer_exam表中失败。");
							examDao.deleteOne(exam.getExam_id());
							return false;
						}
						question_id.add(question.getQuestion_id());
						count++;
					} else {
						// 查找已经完成组卷的题目是否出现重复
						if (!question_id.contains(question.getQuestion_id())) {
							if (shortAnswerExamDao.insertShortAnswer(question) < 1) {
								log.error(exam.getUser_id()
										+ "组卷失败！原因：题目录入shortAnswer_exam表中失败。");
								examDao.deleteOne(exam.getExam_id());
								return false;
							}
							question_id.add(question.getQuestion_id());
							count++;
						}
					}
					if (count == questionCount) {
						break outer;
					}
				}
				flag++;
			}
			if (flag >= max) {
				log.error(exam.getUser_id()
						+ "组卷失败！原因：可能由于题库题目数量太少，导致重组试卷超过3次以上！");
				examDao.deleteOne(exam.getExam_id());
				return false;
			}
		}
		return true;
	}

	@Override
	public void deleteSingle(Long exam_id, Long question_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("exam_id", exam_id);
		params.put("question_id", question_id);
		if (singleExamDao.deleteOne(params) < 1) {
			throw new ServiceException(new ErrorCode(25671,
					"试卷单选题删除失败，请联系管理人员！"));
		}
	}

	@Override
	public void deleteMultiple(Long exam_id, Long question_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("exam_id", exam_id);
		params.put("question_id", question_id);
		if (multipleExamDao.deleteOne(params) < 1) {
			throw new ServiceException(new ErrorCode(25672,
					"试卷多选题删除失败，请联系管理人员！"));
		}
	}

	@Override
	public void deleteJudge(Long exam_id, Long question_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("exam_id", exam_id);
		params.put("question_id", question_id);
		if (judgeExamDao.deleteOne(params) < 1) {
			throw new ServiceException(new ErrorCode(25672,
					"试卷判断题删除失败，请联系管理人员！"));
		}
	}

	@Override
	public void deleteShortAnswer(Long exam_id, Long question_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("exam_id", exam_id);
		params.put("question_id", question_id);
		if (shortAnswerExamDao.deleteOne(params) < 1) {
			throw new ServiceException(new ErrorCode(25674,
					"试卷简答题删除失败，请联系管理人员！"));
		}
	}

	@Override
	public Single insertSingle(Long exam_id, Long question_id) {
		Single single = singleDao.getOne(question_id);
		if (single == null) {
			throw new ServiceException(
					new ErrorCode(29876, "所插入题目不存在，请联系管理人员！"));
		}
		Question question = new Question();
		question.setAnswer(single.getAnswer());
		question.setQuestion_id(question_id);
		question.setTest_id(exam_id);
		if (singleExamDao.insertSingle(question) < 1) {
			throw new ServiceException(new ErrorCode(28971,
					"所插入单选题目失败，请联系管理人员！"));
		}
		return single;
	}

	@Override
	public Multiple insertMultiple(Long exam_id, Long question_id) {
		Multiple multiple = multipleDao.getOne(question_id);
		if (multiple == null) {
			throw new ServiceException(
					new ErrorCode(29876, "所插入题目不存在，请联系管理人员！"));
		}
		Question question = new Question();
		question.setAnswer(multiple.getAnswer());
		question.setQuestion_id(question_id);
		question.setTest_id(exam_id);
		if (multipleExamDao.insertMultiple(question) < 1) {
			throw new ServiceException(new ErrorCode(28972,
					"所插入多选题目失败，请联系管理人员！"));
		}
		return multiple;
	}

	@Override
	public Judge insertJudge(Long exam_id, Long question_id) {
		Judge judge = judgeDao.getOne(question_id);
		if (judge == null) {
			throw new ServiceException(
					new ErrorCode(29876, "所插入题目不存在，请联系管理人员！"));
		}
		Question question = new Question();
		question.setAnswer(judge.getAnswer());
		question.setQuestion_id(question_id);
		question.setTest_id(exam_id);
		if (judgeExamDao.insertJudge(question) < 1) {
			throw new ServiceException(new ErrorCode(28973,
					"所插入判断题目失败，请联系管理人员！"));
		}
		return judge;
	}

	@Override
	public ShortAnswer insertShortAnswer(Long exam_id, Long question_id) {
		ShortAnswer shortAnswer = shortAnswerDao.getOne(question_id);
		if (shortAnswer == null) {
			throw new ServiceException(
					new ErrorCode(29876, "所插入题目不存在，请联系管理人员！"));
		}
		Question question = new Question();
		question.setAnswer(shortAnswer.getAnswer());
		question.setQuestion_id(question_id);
		question.setTest_id(exam_id);
		if (shortAnswerExamDao.insertShortAnswer(question) < 1) {
			throw new ServiceException(new ErrorCode(28974,
					"所插入简答题目失败，请联系管理人员！"));
		}
		return shortAnswer;
	}

	@Override
	public List<OnlineExamView> searchOnlineByTID(String teacher_id,
			String course_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("teacher_id", teacher_id);
		params.put("course_id", course_id);
		return examDao.searchOnlineExamByTID(params);
	}

	@Override
	public List<HistoryExamView> searchHistoryByTID(String teacher_id,
			String course_id) {
		Map<String, Object> params = new HashMap<>();
		params.put("teacher_id", teacher_id);
		params.put("course_id", course_id);
		return examDao.searchHistoryExam(params);
	}

	@Override
	@Transactional
	public ExamPaperView getExamDetailByID(Long exam_id) {
		Exam exam = examDao.getExamById(exam_id);
		Template template = templateDao.getOne(exam.getTemplate_id());
		ExamPaperView examView = new ExamPaperView(exam.getExam_id(),
				exam.getTemplate_id(), exam.getStart_time(),
				exam.getEnd_time(), exam.getUser_id());
		if (template.getSingle_num() > 0) {
			examView.setSingleList(singleExamDao.searchExamSingle(exam
					.getExam_id()));
		}
		if (template.getMultiple_num() > 0) {
			examView.setMultipleList(multipleExamDao.searchExamMultiple(exam
					.getExam_id()));
		}
		if (template.getJudge_num() > 0) {
			examView.setJudgeleList(judgeExamDao.searchExamJudge(exam
					.getExam_id()));
		}
		if (template.getShortAnswer_num() > 0) {
			examView.setShortAnswerleList(shortAnswerExamDao
					.searchExamShort(exam.getExam_id()));
		}
		return examView;
	}

	@Override
	public List<OnlineExamView> searchOnlineBySID(String student_id) {
		return examDao.searchOnlineExamBySID(student_id);
	}

	@Override
	public Exam getExamByID(Long exam_id) {
		return examDao.getExamById(exam_id);
	}

	@Override
	public ExamPaperView getExamDetailByIDNoAnswer(Long exam_id) {
		Exam exam = examDao.getExamById(exam_id);
		Template template = templateDao.getOne(exam.getTemplate_id());
		ExamPaperView examView = new ExamPaperView(exam.getExam_id(),
				exam.getTemplate_id(), exam.getStart_time(),
				exam.getEnd_time(), exam.getUser_id());
		if (template.getSingle_num() > 0) {
			examView.setSingleList(singleExamDao.searchExamSingleNoAnswer(exam
					.getExam_id()));
		}
		if (template.getMultiple_num() > 0) {
			examView.setMultipleList(multipleExamDao
					.searchExamMultipleNoAnswer(exam.getExam_id()));
		}
		if (template.getJudge_num() > 0) {
			examView.setJudgeleList(judgeExamDao.searchExamJudgeNoAnswer(exam
					.getExam_id()));
		}
		if (template.getShortAnswer_num() > 0) {
			examView.setShortAnswerleList(shortAnswerExamDao
					.searchExamShortNoAnswer(exam.getExam_id()));
		}
		return examView;
	}

	@Override
	public void AutoCheckExam(Long exam_id) {
		examDao.updateStatus(exam_id);
		Template template = templateDao.getOneByExamId(exam_id);
		ExamPaperView examView = this.getExamDetailByID(exam_id);
		List<SingleExamView> singleList = examView.getSingleList();
		List<MultipleExamView> multipleList = examView.getMultipleList();
		List<JudgeExamView> judgeList = examView.getJudgeleList();
		Map<String, Object> params = new HashMap<>();
		params.put("exam_id", exam_id);
		Double score = 0.0;
		for (SingleExamView single : singleList) {
			params.put("question_id", single.getQuestion_id());
			if (single.getRightanswer().equals(single.getStuanswer())) {
				params.put("score", template.getSingle_score());
				singleExamDao.updateScore(params);
				score += template.getSingle_score();
			}
		}
		for (MultipleExamView multiple : multipleList) {
			params.put("question_id", multiple.getQuestion_id());
			if (multiple.getRightanswer().equals(multiple.getStuanswer())) {
				params.put("score", template.getMultiple_score());
				singleExamDao.updateScore(params);
				score += template.getMultiple_score();
			}
		}
		for (JudgeExamView judge : judgeList) {
			params.put("question_id", judge.getQuestion_id());
			if (judge.getRightanswer().equals(judge.getStuanswer())) {
				params.put("score", template.getJudge_score());
				singleExamDao.updateScore(params);
				score += template.getJudge_score();
			}
		}

		params.clear();
		params.put("exam_id", exam_id);
		params.put("score", score);
		examDao.updateScore(params);
	}
}
