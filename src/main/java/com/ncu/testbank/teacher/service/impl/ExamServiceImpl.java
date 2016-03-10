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

import com.google.common.collect.Lists;
import com.ncu.testbank.admin.dao.ITeacherDao;
import com.ncu.testbank.base.exception.ErrorCode;
import com.ncu.testbank.base.exception.ServiceException;
import com.ncu.testbank.base.utils.ExamUtils;
import com.ncu.testbank.base.utils.RandomID;
import com.ncu.testbank.base.utils.RandomUtils;
import com.ncu.testbank.teacher.dao.IExamDao;
import com.ncu.testbank.teacher.dao.IJudgeDao;
import com.ncu.testbank.teacher.dao.IMultipleDao;
import com.ncu.testbank.teacher.dao.IShortAnswerDao;
import com.ncu.testbank.teacher.dao.ISingleDao;
import com.ncu.testbank.teacher.dao.ITemplateDao;
import com.ncu.testbank.teacher.data.Exam;
import com.ncu.testbank.teacher.data.Question;
import com.ncu.testbank.teacher.data.QuestionCount;
import com.ncu.testbank.teacher.data.QuestionLevel;
import com.ncu.testbank.teacher.data.Template;
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

	@Override
	public Long createExam(long template_id, String user_id, Timestamp start,
			Timestamp end) {
		Template template = templateDao.getOne(template_id);
		Exam exam = new Exam(RandomID.getID(), template_id, start, end, user_id);
		String pointStr = template.getPoint_id().replaceAll(" ", "");
		String[] pointStrArray = pointStr.split(",");
		List<String> tempList = Lists.newArrayList(pointStrArray);
		List<Long> points = new ArrayList<>();
		for (String s : tempList) {
			points.add(Long.valueOf(s));
		}

		if (examDao.insertOne(exam) < 1) {
			log.error(user_id + "使用者组卷失败！原因：插入exam试卷表出错！");
			// TODO 短信或邮件通知教师组卷失败
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
			this.createSingle(hardCount, hardPoints, hardMap, exam);

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
			this.createSingle(mediumCount, mediumPoints, mediumMap, exam);

			// 最低难度题目组卷
			total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : easyMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (easyCount > total) {
				log.error(user_id + "组卷失败！原因：题库题目数量过少，无法完成组卷！");
				// TODO 发送邮件告知教师组卷失败
				examDao.deleteOne(exam.getExam_id());
				throw new ServiceException(new ErrorCode(60002,
						"组卷失败！原因：题库题目数量过少，无法完成组卷！"));
			}
			this.createSingle(easyCount, easyPoints, easyMap, exam);
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
			this.createMultiple(hardCount, hardPoints, hardMap, exam);

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
			this.createMultiple(mediumCount, mediumPoints, mediumMap, exam);

			// 最低难度题目组卷
			total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : easyMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (easyCount > total) {
				log.error(user_id + "组卷失败！原因：题库题目数量过少，无法完成组卷！");
				// TODO 发送邮件告知教师组卷失败
				examDao.deleteOne(exam.getExam_id());
				throw new ServiceException(new ErrorCode(60002,
						"组卷失败！原因：题库题目数量过少，无法完成组卷！"));
			}
			this.createMultiple(easyCount, easyPoints, easyMap, exam);
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
			this.createJudge(hardCount, hardPoints, hardMap, exam);

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
			this.createJudge(mediumCount, mediumPoints, mediumMap, exam);

			// 最低难度题目组卷
			total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : easyMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (easyCount > total) {
				log.error(user_id + "组卷失败！原因：题库题目数量过少，无法完成组卷！");
				// TODO 发送邮件告知教师组卷失败
				examDao.deleteOne(exam.getExam_id());
				throw new ServiceException(new ErrorCode(60002,
						"组卷失败！原因：题库题目数量过少，无法完成组卷！"));
			}
			this.createJudge(easyCount, easyPoints, easyMap, exam);
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
			this.createShortAnswer(hardCount, hardPoints, hardMap, exam);

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
			this.createShortAnswer(mediumCount, mediumPoints, mediumMap, exam);

			// 最低难度题目组卷
			total = 0; // 对应难度题目总数量
			for (Entry<Long, List<Question>> entry : easyMap.entrySet()) {
				List<Question> questions = entry.getValue();
				total += questions.size();
			}
			// 最高难度题目数量高于题目总数，那么下一级难度题目数量增加
			if (easyCount > total) {
				log.error(user_id + "组卷失败！原因：题库题目数量过少，无法完成组卷！");
				// TODO 发送邮件告知教师组卷失败
				examDao.deleteOne(exam.getExam_id());
				throw new ServiceException(new ErrorCode(60002,
						"组卷失败！原因：题库题目数量过少，无法完成组卷！"));
			}
			this.createShortAnswer(easyCount, easyPoints, easyMap, exam);
		}

		return exam.getExam_id();
	}

	private void createSingle(int questionCount, List<Long> points,
			Map<Long, List<Question>> map, Exam exam) {
		if (questionCount == 1) {
			// 只有一道题目时，随机出题
			int random = RandomUtils.random(0, points.size() - 1);
			long point_id = points.get(random);
			List<Question> questions = map.get(point_id);
			Question question = questions.get(RandomUtils.random(0,
					questions.size() - 1));
			if (examDao.insertSingle(question) < 1) {
				log.error(exam.getUser_id() + "组卷失败！原因：题目录入single_exam表中失败。");
				// TODO 发送邮件告知教师组卷失败
				examDao.deleteOne(exam.getExam_id());
				throw new ServiceException(new ErrorCode(60003,
						"组卷失败！原因：题目录入single_exam表中失败。"));
			}
		} else if (questionCount > 1 && questionCount <= points.size()) {
			// 出题数量大于1，但是小于知识点数量，保证每个题目考点不同
			for (int i = 0; i < questionCount; i++) {
				List<Question> questions = map.get(points.get(i));
				Question question = questions.get(RandomUtils.random(0,
						questions.size() - 1));
				if (examDao.insertSingle(question) < 1) {
					log.error(exam.getUser_id()
							+ "组卷失败！原因：题目录入single_exam表中失败。");
					// TODO 发送邮件告知教师组卷失败
					examDao.deleteOne(exam.getExam_id());
					throw new ServiceException(new ErrorCode(60003,
							"组卷失败！原因：题目录入single_exam表中失败。"));
				}
			}
		} else if (questionCount > points.size()) {
			// 出题数量大于题目考点数量，需要检测是否有重题，反复组卷。
			// TODO 三次依然出现重题失败组卷，那么发送邮件告知教师组卷失败
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
						if (examDao.insertSingle(question) < 1) {
							log.error(exam.getUser_id()
									+ "组卷失败！原因：题目录入single_exam表中失败。");
							// TODO 发送邮件告知教师组卷失败
							examDao.deleteOne(exam.getExam_id());
							throw new ServiceException(new ErrorCode(60003,
									"组卷失败！原因：题目录入single_exam表中失败。"));
						}
						question_id.add(question.getQuestion_id());
						count++;
					} else {
						// 查找已经完成组卷的题目是否出现重复
						if (!question_id.contains(question.getQuestion_id())) {
							if (examDao.insertSingle(question) < 1) {
								log.error(exam.getUser_id()
										+ "组卷失败！原因：题目录入single_exam表中失败。");
								// TODO 发送邮件告知教师组卷失败
								examDao.deleteOne(exam.getExam_id());
								throw new ServiceException(new ErrorCode(60003,
										"组卷失败！原因：题目录入single_exam表中失败。"));
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
				// TODO 发送邮件告知教师组卷失败
				examDao.deleteOne(exam.getExam_id());
				throw new ServiceException(new ErrorCode(60004,
						"组卷失败！原因：可能由于题库题目数量太少，导致重组试卷超过3次以上！"));
			}
		}
	}

	private void createMultiple(int questionCount, List<Long> points,
			Map<Long, List<Question>> map, Exam exam) {
		if (questionCount == 1) {
			// 只有一道题目时，随机出题
			int random = RandomUtils.random(0, points.size() - 1);
			long point_id = points.get(random);
			List<Question> questions = map.get(point_id);
			Question question = questions.get(RandomUtils.random(0,
					questions.size() - 1));
			if (examDao.insertMultiple(question) < 1) {
				log.error(exam.getUser_id() + "组卷失败！原因：题目录入multiple_exam表中失败。");
				// TODO 发送邮件告知教师组卷失败
				examDao.deleteOne(exam.getExam_id());
				throw new ServiceException(new ErrorCode(60005,
						"组卷失败！原因：题目录入multiple_exam表中失败。"));
			}
		} else if (questionCount > 1 && questionCount <= points.size()) {
			// 出题数量大于1，但是小于知识点数量，保证每个题目考点不同
			for (int i = 0; i < questionCount; i++) {
				List<Question> questions = map.get(points.get(i));
				Question question = questions.get(RandomUtils.random(0,
						questions.size() - 1));
				if (examDao.insertMultiple(question) < 1) {
					log.error(exam.getUser_id()
							+ "组卷失败！原因：题目录入multiple_exam表中失败。");
					// TODO 发送邮件告知教师组卷失败
					examDao.deleteOne(exam.getExam_id());
					throw new ServiceException(new ErrorCode(60005,
							"组卷失败！原因：题目录入multiple_exam表中失败。"));
				}
			}
		} else if (questionCount > points.size()) {
			// 出题数量大于题目考点数量，需要检测是否有重题，反复组卷。
			// TODO 三次依然出现重题失败组卷，那么发送邮件告知教师组卷失败
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
						if (examDao.insertMultiple(question) < 1) {
							log.error(exam.getUser_id()
									+ "组卷失败！原因：题目录入multiple_exam表中失败。");
							// TODO 发送邮件告知教师组卷失败
							examDao.deleteOne(exam.getExam_id());
							throw new ServiceException(new ErrorCode(60005,
									"组卷失败！原因：题目录入multiple_exam表中失败。"));
						}
						question_id.add(question.getQuestion_id());
						count++;
					} else {
						// 查找已经完成组卷的题目是否出现重复
						if (!question_id.contains(question.getQuestion_id())) {
							if (examDao.insertMultiple(question) < 1) {
								log.error(exam.getUser_id()
										+ "组卷失败！原因：题目录入multiple_exam表中失败。");
								// TODO 发送邮件告知教师组卷失败
								examDao.deleteOne(exam.getExam_id());
								throw new ServiceException(new ErrorCode(60005,
										"组卷失败！原因：题目录入multiple_exam表中失败。"));
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
				// TODO 发送邮件告知教师组卷失败
				examDao.deleteOne(exam.getExam_id());
				throw new ServiceException(new ErrorCode(60004,
						"组卷失败！原因：可能由于题库题目数量太少，导致重组试卷超过3次以上！"));
			}
		}
	}

	private void createJudge(int questionCount, List<Long> points,
			Map<Long, List<Question>> map, Exam exam) {
		if (questionCount == 1) {
			// 只有一道题目时，随机出题
			int random = RandomUtils.random(0, points.size() - 1);
			long point_id = points.get(random);
			List<Question> questions = map.get(point_id);
			Question question = questions.get(RandomUtils.random(0,
					questions.size() - 1));
			if (examDao.insertJudge(question) < 1) {
				log.error(exam.getUser_id() + "组卷失败！原因：题目录入judge_exam表中失败。");
				// TODO 发送邮件告知教师组卷失败
				examDao.deleteOne(exam.getExam_id());
				throw new ServiceException(new ErrorCode(60006,
						"组卷失败！原因：题目录入judge_exam表中失败。"));
			}
		} else if (questionCount > 1 && questionCount <= points.size()) {
			// 出题数量大于1，但是小于知识点数量，保证每个题目考点不同
			for (int i = 0; i < questionCount; i++) {
				List<Question> questions = map.get(points.get(i));
				Question question = questions.get(RandomUtils.random(0,
						questions.size() - 1));
				if (examDao.insertJudge(question) < 1) {
					log.error(exam.getUser_id() + "组卷失败！原因：题目录入judge_exam表中失败。");
					// TODO 发送邮件告知教师组卷失败
					examDao.deleteOne(exam.getExam_id());
					throw new ServiceException(new ErrorCode(60006,
							"组卷失败！原因：题目录入judge_exam表中失败。"));
				}
			}
		} else if (questionCount > points.size()) {
			// 出题数量大于题目考点数量，需要检测是否有重题，反复组卷。
			// TODO 三次依然出现重题失败组卷，那么发送邮件告知教师组卷失败
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
						if (examDao.insertJudge(question) < 1) {
							log.error(exam.getUser_id()
									+ "组卷失败！原因：题目录入judge_exam表中失败。");
							// TODO 发送邮件告知教师组卷失败
							examDao.deleteOne(exam.getExam_id());
							throw new ServiceException(new ErrorCode(60006,
									"组卷失败！原因：题目录入judge_exam表中失败。"));
						}
						question_id.add(question.getQuestion_id());
						count++;
					} else {
						// 查找已经完成组卷的题目是否出现重复
						if (!question_id.contains(question.getQuestion_id())) {
							if (examDao.insertJudge(question) < 1) {
								log.error(exam.getUser_id()
										+ "组卷失败！原因：题目录入judge_exam表中失败。");
								// TODO 发送邮件告知教师组卷失败
								examDao.deleteOne(exam.getExam_id());
								throw new ServiceException(new ErrorCode(60006,
										"组卷失败！原因：题目录入judge_exam表中失败。"));
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
				// TODO 发送邮件告知教师组卷失败
				examDao.deleteOne(exam.getExam_id());
				throw new ServiceException(new ErrorCode(60004,
						"组卷失败！原因：可能由于题库题目数量太少，导致重组试卷超过3次以上！"));
			}
		}
	}

	private void createShortAnswer(int questionCount, List<Long> points,
			Map<Long, List<Question>> map, Exam exam) {
		if (questionCount == 1) {
			// 只有一道题目时，随机出题
			int random = RandomUtils.random(0, points.size() - 1);
			long point_id = points.get(random);
			List<Question> questions = map.get(point_id);
			Question question = questions.get(RandomUtils.random(0,
					questions.size() - 1));
			if (examDao.insertShortAnswer(question) < 1) {
				log.error(exam.getUser_id()
						+ "组卷失败！原因：题目录入shortAnswer_exam表中失败。");
				// TODO 发送邮件告知教师组卷失败
				examDao.deleteOne(exam.getExam_id());
				throw new ServiceException(new ErrorCode(60007,
						"组卷失败！原因：题目录入shortAnswer_exam表中失败。"));
			}
		} else if (questionCount > 1 && questionCount <= points.size()) {
			// 出题数量大于1，但是小于知识点数量，保证每个题目考点不同
			for (int i = 0; i < questionCount; i++) {
				List<Question> questions = map.get(points.get(i));
				Question question = questions.get(RandomUtils.random(0,
						questions.size() - 1));
				if (examDao.insertShortAnswer(question) < 1) {
					log.error(exam.getUser_id()
							+ "组卷失败！原因：题目录入shortAnswer_exam表中失败。");
					// TODO 发送邮件告知教师组卷失败
					examDao.deleteOne(exam.getExam_id());
					throw new ServiceException(new ErrorCode(60007,
							"组卷失败！原因：题目录入shortAnswer_exam表中失败。"));
				}
			}
		} else if (questionCount > points.size()) {
			// 出题数量大于题目考点数量，需要检测是否有重题，反复组卷。
			// TODO 三次依然出现重题失败组卷，那么发送邮件告知教师组卷失败
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
						if (examDao.insertShortAnswer(question) < 1) {
							log.error(exam.getUser_id()
									+ "组卷失败！原因：题目录入shortAnswer_exam表中失败。");
							// TODO 发送邮件告知教师组卷失败
							examDao.deleteOne(exam.getExam_id());
							throw new ServiceException(new ErrorCode(60007,
									"组卷失败！原因：题目录入shortAnswer_exam表中失败。"));
						}
						question_id.add(question.getQuestion_id());
						count++;
					} else {
						// 查找已经完成组卷的题目是否出现重复
						if (!question_id.contains(question.getQuestion_id())) {
							if (examDao.insertShortAnswer(question) < 1) {
								log.error(exam.getUser_id()
										+ "组卷失败！原因：题目录入shortAnswer_exam表中失败。");
								// TODO 发送邮件告知教师组卷失败
								examDao.deleteOne(exam.getExam_id());
								throw new ServiceException(new ErrorCode(60007,
										"组卷失败！原因：题目录入shortAnswer_exam表中失败。"));
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
				// TODO 发送邮件告知教师组卷失败
				examDao.deleteOne(exam.getExam_id());
				throw new ServiceException(new ErrorCode(60004,
						"组卷失败！原因：可能由于题库题目数量太少，导致重组试卷超过3次以上！"));
			}
		}
	}
}
